#!/usr/bin/env bash
set -Eeuo pipefail

REPO="${REPO:-madebycli/Kiyori}"
ENVIRONMENT="${ENVIRONMENT:-release}"
KEYSTORE_PATH="${KEYSTORE_PATH:-${1:-$HOME/.local/share/kiyori-signing/kiyori-release.jks}}"
KEY_ALIAS="${KEY_ALIAS:-kiyori-release}"
CREATE_KEYSTORE="${CREATE_KEYSTORE:-0}"

usage() {
  cat <<'USAGE'
Usage:
  nix-shell -p gh jdk17_headless openssl coreutils gnused --run \
    'bash scripts/setup-release-secrets-nixos.sh [KEYSTORE_PATH]'

Environment overrides:
  REPO=madebycli/Kiyori
  ENVIRONMENT=release
  KEYSTORE_PATH=/path/to/existing-or-new.jks
  KEY_ALIAS=kiyori-release
  CREATE_KEYSTORE=1   # explicitly allow creating a new permanent signing key

Important:
  Reuse the original production keystore when one already exists. Android updates
  signed with a different key cannot replace an installed production build.
USAGE
}

case "${1:-}" in
  -h|--help) usage; exit 0 ;;
esac

for cmd in gh keytool openssl base64 tr sed chmod mkdir; do
  command -v "$cmd" >/dev/null || {
    echo "Missing command: $cmd" >&2
    echo "Run this script through the nix-shell command shown in --help." >&2
    exit 2
  }
done

gh auth status --hostname github.com >/dev/null
gh api "repos/$REPO" --jq .full_name >/dev/null

read_secret_twice() {
  local variable_name="$1"
  local label="$2"
  local first second
  while true; do
    read -r -s -p "$label: " first
    echo
    read -r -s -p "$label wiederholen: " second
    echo
    if [[ -n "$first" && "$first" == "$second" ]]; then
      printf -v "$variable_name" '%s' "$first"
      return 0
    fi
    echo "Die Eingaben waren leer oder stimmten nicht überein. Bitte erneut versuchen." >&2
  done
}

mkdir -p "$(dirname "$KEYSTORE_PATH")"
chmod 700 "$(dirname "$KEYSTORE_PATH")"

if [[ ! -f "$KEYSTORE_PATH" ]]; then
  if [[ "$CREATE_KEYSTORE" != "1" ]]; then
    cat >&2 <<EOF2
Keystore nicht gefunden: $KEYSTORE_PATH

Falls bereits eine veröffentlichte Kiyori-App existiert, hier unbedingt deren
ursprünglichen Keystore verwenden. Für einen neuen dauerhaften Release-Key den
Befehl mit CREATE_KEYSTORE=1 erneut ausführen.
EOF2
    exit 3
  fi

  echo "Erzeuge einen neuen DAUERHAFTEN Release-Keystore: $KEYSTORE_PATH"
  read_secret_twice KEYSTORE_PASSWORD "Keystore-Passwort"
  read_secret_twice KEY_PASSWORD "Key-Passwort"

  keytool -genkeypair \
    -noprompt \
    -storetype JKS \
    -keystore "$KEYSTORE_PATH" \
    -storepass "$KEYSTORE_PASSWORD" \
    -alias "$KEY_ALIAS" \
    -keypass "$KEY_PASSWORD" \
    -keyalg RSA \
    -keysize 4096 \
    -sigalg SHA256withRSA \
    -validity 9125 \
    -dname "CN=Kiyori Release, OU=Android, O=Kiyori, L=Berlin, C=DE"
  chmod 600 "$KEYSTORE_PATH"
else
  echo "Verwende vorhandenen Keystore: $KEYSTORE_PATH"
  read_secret_twice KEYSTORE_PASSWORD "Keystore-Passwort"
  read_secret_twice KEY_PASSWORD "Key-Passwort"
fi

keytool -list \
  -keystore "$KEYSTORE_PATH" \
  -storepass "$KEYSTORE_PASSWORD" \
  -alias "$KEY_ALIAS" >/dev/null

SIGNING_CERT_SHA256="$(
  keytool -exportcert -rfc \
    -keystore "$KEYSTORE_PATH" \
    -storepass "$KEYSTORE_PASSWORD" \
    -alias "$KEY_ALIAS" \
  | openssl x509 -noout -fingerprint -sha256 \
  | sed 's/^[^=]*=//' \
  | tr '[:upper:]' '[:lower:]' \
  | tr -d ':[:space:]'
)"
[[ "$SIGNING_CERT_SHA256" =~ ^[0-9a-f]{64}$ ]]

# Ensure the protected GitHub Actions environment exists.
gh api --method PUT "repos/$REPO/environments/$ENVIRONMENT" >/dev/null

set_secret() {
  local name="$1"
  local value="$2"
  printf '%s' "$value" | gh secret set "$name" --repo "$REPO" --env "$ENVIRONMENT"
}

base64 < "$KEYSTORE_PATH" | tr -d '\n' \
  | gh secret set KEYSTORE_FILE --repo "$REPO" --env "$ENVIRONMENT"
set_secret KEYSTORE_PASSWORD "$KEYSTORE_PASSWORD"
set_secret KEY_ALIAS "$KEY_ALIAS"
set_secret KEY_PASSWORD "$KEY_PASSWORD"
set_secret SIGNING_CERT_SHA256 "$SIGNING_CERT_SHA256"

unset KEYSTORE_PASSWORD KEY_PASSWORD

echo
echo "Release-Secrets im Environment '$ENVIRONMENT' gesetzt:"
gh secret list --repo "$REPO" --env "$ENVIRONMENT" \
  --json name --jq '.[].name' | sort

echo
echo "Zertifikat SHA-256: $SIGNING_CERT_SHA256"
echo "Keystore sicher und redundant sichern: $KEYSTORE_PATH"
echo "Danach PR #6 mergen und den Workflow 'Signed GitHub Release' manuell starten."
