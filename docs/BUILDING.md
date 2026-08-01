# Building Navori

This fork uses `navori/develop` for Navori development. Keep `develop` and `stable` available for synchronizing with the upstream repository.

## 1. Make `navori/develop` the default branch

The manual GitHub Actions branch selector starts on the repository default branch. Change it once in GitHub:

1. Open **Settings → General → Default branch**.
2. Select `navori/develop` and confirm the change.

The same change can be made from a terminal with GitHub CLI:

```bash
gh auth login
gh api --method PATCH repos/xnixjoyer/Navori \
  -f default_branch='navori/develop'
```

## 2. Enter the NixOS build environment

The repository contains a `shell.nix` with JDK 17, Android platform 37, Android Build Tools 36.0.0, GitHub CLI, and the NixOS `aapt2` override required by Android Gradle Plugin builds.

```bash
git clone https://github.com/xnixjoyer/Navori.git
cd Navori
git switch navori/develop
nix-shell
```

If Android license evaluation is disabled in your global Nix configuration, create `~/.config/nixpkgs/config.nix`:

```nix
{
  allowUnfree = true;
  android_sdk.accept_license = true;
}
```

Then start `nix-shell` again. If API 37 is missing from an old channel, update the channel first:

```bash
sudo nix-channel --update
nix-shell
```

## 3. Build an installable debug APK

No private signing key is required for a debug build.

```bash
./gradlew \
  :app:assembleFossDebug \
  :app:testFossDebugUnitTest \
  --no-daemon \
  --stacktrace

find app/build/outputs/apk/foss/debug -name '*.apk' -print
```

The universal debug APK can be installed with:

```bash
adb install -r app/build/outputs/apk/foss/debug/*universal*.apk
```

## 4. Generate the release signing key

Never commit the keystore or passwords. The repository ignores `.secrets`, `*.jks`, and `*.keystore`.

```bash
install -d -m 700 .secrets

read -rsp 'Keystore password: ' KEYSTORE_PASSWORD; echo
read -rsp 'Key password: ' KEY_PASSWORD; echo
KEY_ALIAS='navori'
export KEYSTORE_PASSWORD KEY_PASSWORD KEY_ALIAS

keytool -genkeypair \
  -keystore .secrets/navori-release.jks \
  -storetype JKS \
  -storepass:env KEYSTORE_PASSWORD \
  -alias "$KEY_ALIAS" \
  -keypass:env KEY_PASSWORD \
  -keyalg RSA \
  -keysize 4096 \
  -validity 10000 \
  -dname 'CN=Navori, OU=Android, O=Navori, L=Berlin, ST=Berlin, C=DE'

keytool -list \
  -keystore .secrets/navori-release.jks \
  -storepass:env KEYSTORE_PASSWORD
```

Back up `.secrets/navori-release.jks` and the passwords in a password manager. Losing this key prevents future updates from being signed with the same identity.

## 5. Upload signing secrets to GitHub

Run these commands in the same shell in which the password variables were set:

```bash
gh auth status || gh auth login

base64 --wrap=0 .secrets/navori-release.jks \
  | gh secret set KEYSTORE_FILE --repo xnixjoyer/Navori

printf '%s' "$KEYSTORE_PASSWORD" \
  | gh secret set KEYSTORE_PASSWORD --repo xnixjoyer/Navori

printf '%s' "$KEY_ALIAS" \
  | gh secret set KEY_ALIAS --repo xnixjoyer/Navori

printf '%s' "$KEY_PASSWORD" \
  | gh secret set KEY_PASSWORD --repo xnixjoyer/Navori

gh secret list --repo xnixjoyer/Navori
```

## 6. Build a signed release locally

```bash
KEYSTORE_FILE="$PWD/.secrets/navori-release.jks"

read -rsp 'Keystore password: ' KEYSTORE_PASSWORD; echo
read -rsp 'Key password: ' KEY_PASSWORD; echo
KEY_ALIAS='navori'

chmod +x ./gradlew

env \
  "ORG_GRADLE_PROJECT_android.injected.signing.store.file=$KEYSTORE_FILE" \
  "ORG_GRADLE_PROJECT_android.injected.signing.store.password=$KEYSTORE_PASSWORD" \
  "ORG_GRADLE_PROJECT_android.injected.signing.key.alias=$KEY_ALIAS" \
  "ORG_GRADLE_PROJECT_android.injected.signing.key.password=$KEY_PASSWORD" \
  ./gradlew \
    :app:assembleFossRelease \
    --no-daemon \
    --stacktrace

for apk in app/build/outputs/apk/foss/release/*.apk; do
  apksigner verify --verbose "$apk"
done

find app/build/outputs/apk/foss/release -name '*.apk' -print
```

## 7. Build with GitHub Actions

Every push and pull request targeting `navori/develop` builds and tests a FOSS debug APK without access to signing secrets.

For a signed release:

1. Open **Actions → Android CI and signed release**.
2. Select **Run workflow**.
3. Keep `navori/develop` selected.
4. Leave **Build a signed FOSS release** enabled.
5. Leave **Upload only the universal release APK** enabled unless ABI-specific APKs are needed.
6. Download the `navori-foss-release-…` artifact after the workflow succeeds.

The release job refuses to run from another branch and verifies every generated APK with `apksigner` before upload.

## 8. Optional Crowdin setup

Crowdin synchronization is manual and requires only these repository secrets:

- `CROWDIN_PROJECT_ID`
- `CROWDIN_PERSONAL_TOKEN`

The workflow uses the repository-scoped `GITHUB_TOKEN`; a separate broad `GH_TOKEN` secret is not required. In **Settings → Actions → General**, enable **Allow GitHub Actions to create and approve pull requests** so the Crowdin action can open its translation pull request against `navori/develop`.
