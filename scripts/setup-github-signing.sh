#!/usr/bin/env bash
set -Eeuo pipefail

umask 077

REPOSITORY="${1:-madebycli/Kiyori}"
OUTPUT_DIR="${2:-$HOME/.local/share/kiyori-signing}"
RELEASE_ENVIRONMENT="${RELEASE_ENVIRONMENT:-release}"
KEYSTORE_PATH="$OUTPUT_DIR/kiyori-release.p12"
CERTIFICATE_PATH="$OUTPUT_DIR/kiyori-release-certificate.pem"
DEFAULT_ALIAS="kiyori-release"
DEFAULT_DNAME="CN=Kiyori Release, O=Kiyori, C=DE"

for command in gh keytool base64 grep tr; do
  command -v "$command" >/dev/null 2>&1 || {
    echo "Missing required command: $command" >&2
    exit 2
  }
done

if ! gh auth status >/dev/null 2>&1; then
  echo "GitHub CLI is not authenticated. Starting login..."
  gh auth login
fi
gh auth status >/dev/null

mkdir -p "$OUTPUT_DIR"
chmod 700 "$OUTPUT_DIR"

if [ -e "$CERTIFICATE_PATH" ] && [ ! -f "$KEYSTORE_PATH" ]; then
  echo "Certificate exists, but the private keystore is missing:" >&2
  echo "  $CERTIFICATE_PATH" >&2
  echo "Restore the matching keystore before continuing." >&2
  exit 3
fi

read -r -p "Key alias [$DEFAULT_ALIAS]: " KEY_ALIAS
KEY_ALIAS="${KEY_ALIAS:-$DEFAULT_ALIAS}"

if [ -f "$KEYSTORE_PATH" ]; then
  echo "Existing keystore found; it will be reused: $KEYSTORE_PATH"
  read -r -s -p "Existing keystore password: " STORE_PASSWORD
  echo
  trap 'unset STORE_PASSWORD' EXIT

  if ! keytool -list \
    -keystore "$KEYSTORE_PATH" \
    -storepass "$STORE_PASSWORD" \
    -alias "$KEY_ALIAS" >/dev/null; then
    echo "The password or key alias is not valid for the existing keystore." >&2
    exit 4
  fi
else
  read -r -p "Certificate identity [$DEFAULT_DNAME]: " CERTIFICATE_DNAME
  CERTIFICATE_DNAME="${CERTIFICATE_DNAME:-$DEFAULT_DNAME}"

  read -r -s -p "New keystore password: " STORE_PASSWORD
  echo
  read -r -s -p "Repeat keystore password: " CONFIRM_PASSWORD
  echo

  if [ "$STORE_PASSWORD" != "$CONFIRM_PASSWORD" ]; then
    echo "Passwords do not match." >&2
    exit 5
  fi
  if [ "${#STORE_PASSWORD}" -lt 16 ]; then
    echo "Use a password with at least 16 characters." >&2
    exit 6
  fi

  trap 'unset STORE_PASSWORD CONFIRM_PASSWORD' EXIT

  keytool -genkeypair \
    -keystore "$KEYSTORE_PATH" \
    -storetype PKCS12 \
    -storepass "$STORE_PASSWORD" \
    -keypass "$STORE_PASSWORD" \
    -alias "$KEY_ALIAS" \
    -keyalg RSA \
    -keysize 4096 \
    -validity 10000 \
    -dname "$CERTIFICATE_DNAME"
fi

certificate_tmp="$CERTIFICATE_PATH.tmp"
rm -f "$certificate_tmp"
keytool -exportcert \
  -rfc \
  -keystore "$KEYSTORE_PATH" \
  -storepass "$STORE_PASSWORD" \
  -alias "$KEY_ALIAS" \
  -file "$certificate_tmp"
mv "$certificate_tmp" "$CERTIFICATE_PATH"
chmod 600 "$KEYSTORE_PATH" "$CERTIFICATE_PATH"

# Signing secrets are environment-scoped. The release workflow itself must be
# dispatched from main, while its source_ref input can select another commit.
# prevent_self_review is intentionally omitted because GitHub only accepts that
# field when at least one required reviewer is configured.
gh api --method PUT \
  "repos/$REPOSITORY/environments/$RELEASE_ENVIRONMENT" \
  --input - >/dev/null <<'JSON'
{
  "wait_timer": 0,
  "deployment_branch_policy": {
    "protected_branches": false,
    "custom_branch_policies": true
  }
}
JSON

if ! gh api \
  "repos/$REPOSITORY/environments/$RELEASE_ENVIRONMENT/deployment-branch-policies" \
  --jq '.branch_policies[]?.name' 2>/dev/null | grep -Fxq main; then
  gh api --method POST \
    "repos/$REPOSITORY/environments/$RELEASE_ENVIRONMENT/deployment-branch-policies" \
    -f name=main \
    -f type=branch >/dev/null
fi

base64 < "$KEYSTORE_PATH" | tr -d '\n' \
  | gh secret set KEYSTORE_FILE --env "$RELEASE_ENVIRONMENT" -R "$REPOSITORY"
printf '%s' "$STORE_PASSWORD" \
  | gh secret set KEYSTORE_PASSWORD --env "$RELEASE_ENVIRONMENT" -R "$REPOSITORY"
printf '%s' "$KEY_ALIAS" \
  | gh secret set KEY_ALIAS --env "$RELEASE_ENVIRONMENT" -R "$REPOSITORY"
printf '%s' "$STORE_PASSWORD" \
  | gh secret set KEY_PASSWORD --env "$RELEASE_ENVIRONMENT" -R "$REPOSITORY"

# Remove obsolete repository-wide copies if an older setup was used.
for secret in KEYSTORE_FILE KEYSTORE_PASSWORD KEY_ALIAS KEY_PASSWORD; do
  gh secret delete "$secret" -R "$REPOSITORY" >/dev/null 2>&1 || true
done

echo
echo "Uploaded GitHub Actions secrets to environment '$RELEASE_ENVIRONMENT':"
gh secret list --env "$RELEASE_ENVIRONMENT" -R "$REPOSITORY" \
  | grep -E '^(KEYSTORE_FILE|KEYSTORE_PASSWORD|KEY_ALIAS|KEY_PASSWORD)[[:space:]]' || true

echo
echo "Certificate fingerprints:"
keytool -list -v \
  -keystore "$KEYSTORE_PATH" \
  -storepass "$STORE_PASSWORD" \
  -alias "$KEY_ALIAS" | grep -E 'SHA1:|SHA256:' || true

echo
echo "Keystore:   $KEYSTORE_PATH"
echo "Certificate: $CERTIFICATE_PATH"
echo "Back up the keystore and password offline. Never commit the keystore."
