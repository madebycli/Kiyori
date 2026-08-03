#!/usr/bin/env bash
set -Eeuo pipefail

umask 077

REPOSITORY="${1:-madebycli/Kiyori}"
OUTPUT_DIR="${2:-$HOME/.local/share/kiyori-signing}"
KEYSTORE_PATH="$OUTPUT_DIR/kiyori-release.p12"
CERTIFICATE_PATH="$OUTPUT_DIR/kiyori-release-certificate.pem"
DEFAULT_ALIAS="kiyori-release"
DEFAULT_DNAME="CN=Kiyori Release, O=Kiyori, C=DE"

for command in gh keytool base64 tr; do
  command -v "$command" >/dev/null 2>&1 || {
    echo "Missing required command: $command" >&2
    exit 2
  }
done

gh auth status >/dev/null
mkdir -p "$OUTPUT_DIR"
chmod 700 "$OUTPUT_DIR"

if [ -e "$KEYSTORE_PATH" ]; then
  echo "Refusing to overwrite existing keystore: $KEYSTORE_PATH" >&2
  exit 3
fi

read -r -p "Key alias [$DEFAULT_ALIAS]: " KEY_ALIAS
KEY_ALIAS="${KEY_ALIAS:-$DEFAULT_ALIAS}"

read -r -p "Certificate identity [$DEFAULT_DNAME]: " CERTIFICATE_DNAME
CERTIFICATE_DNAME="${CERTIFICATE_DNAME:-$DEFAULT_DNAME}"

read -r -s -p "New keystore password: " STORE_PASSWORD
echo
read -r -s -p "Repeat keystore password: " CONFIRM_PASSWORD
echo

if [ "$STORE_PASSWORD" != "$CONFIRM_PASSWORD" ]; then
  echo "Passwords do not match." >&2
  exit 4
fi
if [ "${#STORE_PASSWORD}" -lt 16 ]; then
  echo "Use a password with at least 16 characters." >&2
  exit 5
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

keytool -exportcert \
  -rfc \
  -keystore "$KEYSTORE_PATH" \
  -storepass "$STORE_PASSWORD" \
  -alias "$KEY_ALIAS" \
  -file "$CERTIFICATE_PATH"

base64 < "$KEYSTORE_PATH" | tr -d '\n' | gh secret set KEYSTORE_FILE -R "$REPOSITORY"
printf '%s' "$STORE_PASSWORD" | gh secret set KEYSTORE_PASSWORD -R "$REPOSITORY"
printf '%s' "$KEY_ALIAS" | gh secret set KEY_ALIAS -R "$REPOSITORY"
printf '%s' "$STORE_PASSWORD" | gh secret set KEY_PASSWORD -R "$REPOSITORY"

echo
echo "Uploaded GitHub Actions secrets to $REPOSITORY:"
gh secret list -R "$REPOSITORY" | grep -E '^(KEYSTORE_FILE|KEYSTORE_PASSWORD|KEY_ALIAS|KEY_PASSWORD)[[:space:]]' || true

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
