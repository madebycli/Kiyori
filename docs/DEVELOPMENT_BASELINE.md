# Kiyori development baseline

This document describes the repository baseline before product reconstruction and branding work.

## Branch policy

| Branch | Purpose | Keep? |
|---|---|---|
| `main` | Default branch, verified repository baseline, CI and release workflows | Yes |
| `feature/kiyori-integrated-rebuild` | Active product reconstruction work | Yes |
| `develop` | Unmodified upstream comparison and update source | Yes, while upstream syncing is needed |
| `recovery/phase0-backup` | Historical recovery snapshot | Keep until the first verified signed Kiyori release, then tag/archive and delete the branch |
| `master` | Superseded historical default and transfer scaffolding | Safe to delete after the current baseline is verified |

Development work belongs on `feature/kiyori-integrated-rebuild`. Open pull requests from that branch into `main`. Do not implement new Kiyori features directly on `develop`.

## Android SDK preview handling

The project currently compiles against Android API 37 / Android 17 Preview. Google can publish the preview platform under different SDK Manager paths, including `platforms;android-37`, `platforms;android-37.0`, or `platforms;android-CinnamonBun`.

All Android workflows call `scripts/install-android-sdk.sh`. The script reads the package list from the Canary channel, selects the published Android 17 platform path, installs the newest available Build Tools 37 package, and exports `ANDROID_BUILD_TOOLS_VERSION` for later steps. Do not duplicate hard-coded SDK installation commands in individual workflows.

## Workflows

### Android CI

File: `.github/workflows/android-ci.yml`

Runs on pushes to `main`, pull requests into `main`, and manual dispatch. It builds the FOSS and GMS phone debug variants, runs unit tests and lint, and compiles a FOSS phone release variant without publishing it.

The Wear OS module is intentionally not part of the baseline CI and cannot block phone development.

### Debug APKs

File: `.github/workflows/debug-apks.yml`

Manual workflow. Select any branch in the GitHub **Run workflow** menu and choose `all`, `foss`, or `gms`. It produces only phone APKs. The debug APKs and `SHA256SUMS.txt` are available as a workflow artifact.

Debug APKs use Android's generated debug signing key. They are for installation and testing, not distribution.

### Signed GitHub Release

File: `.github/workflows/release.yml`

For security, always start the workflow itself from `main`. Enter the branch, tag, or commit to build in the `source_ref` input. This keeps the trusted workflow definition and protected signing environment on `main` while allowing a release candidate to be built from another source ref.

The workflow:

1. resolves the requested source ref to an exact commit;
2. verifies that the release tag does not already exist;
3. verifies all signing secrets in the protected `release` environment;
4. builds signed FOSS and GMS phone release APKs;
5. verifies every APK with `apksigner`;
6. generates `SHA256SUMS.txt`;
7. creates a GitHub Release and uploads the APKs and checksums.

A release whose `source_ref` is not `main` is always marked as a prerelease. Draft mode is enabled by default.

Required secrets in the GitHub environment named `release`:

- `KEYSTORE_FILE`: base64-encoded PKCS#12 keystore
- `KEYSTORE_PASSWORD`: keystore password
- `KEY_ALIAS`: signing key alias
- `KEY_PASSWORD`: signing key password

The `release` environment is restricted to workflow runs dispatched from `main`. Do not store these values as repository-wide secrets.

### Crowdin Sync

File: `.github/workflows/crowdin.yml`

Runs only in `madebycli/Kiyori`, only after the source strings file changes on `main`. It has no manual dispatch and does not execute for fork pull requests or arbitrary feature-branch pushes.

Required repository secrets:

- `CROWDIN_PROJECT_ID`
- `CROWDIN_PERSONAL_TOKEN`

The workflow uses GitHub's short-lived scoped workflow token instead of a personal GitHub token. If Crowdin is not configured yet, the workflow remains dormant until the source strings file changes on `main`.

## Create and upload signing keys on NixOS

The private signing key must be generated on your own NixOS machine. It must never be generated in GitHub Actions, committed to Git, or sent through chat.

Clone or update the repository, enter it, and run:

```bash
bash scripts/setup-github-signing-nixos.sh madebycli/Kiyori
```

The wrapper invokes this temporary shell automatically:

```bash
nix-shell -p bash git gh jdk17_headless coreutils gnugrep
```

Inside that shell it executes `scripts/setup-github-signing.sh`. GitHub CLI asks you to authenticate when necessary. The script asks for the alias, certificate identity, and a password with at least 16 characters.

It generates locally:

- `~/.local/share/kiyori-signing/kiyori-release.p12`
- `~/.local/share/kiyori-signing/kiyori-release-certificate.pem`

It then creates the protected GitHub environment `release`, restricts it to workflow runs from `main`, uploads the four environment secrets, removes obsolete repository-wide signing secrets, and prints SHA-1/SHA-256 certificate fingerprints.

Back up the `.p12` file and its password in at least two encrypted offline locations. Losing the signing key prevents future APK updates under the same application identity. Never add a keystore to Git, chat, email, or a GitHub Release.

Verify that the environment secret names exist:

```bash
gh secret list --env release -R madebycli/Kiyori
```

GitHub never shows secret values after upload.

### Equivalent manual `nix-shell -p` command

```bash
nix-shell -p bash git gh jdk17_headless coreutils gnugrep \
  --run 'bash scripts/setup-github-signing.sh madebycli/Kiyori'
```

## Manual workflow commands

Run Android CI from the CLI:

```bash
gh workflow run android-ci.yml -R madebycli/Kiyori --ref main
```

Run the phone debug workflow from the CLI:

```bash
gh workflow run debug-apks.yml -R madebycli/Kiyori --ref main -f variant=all
```

Run a draft signed release from trusted workflow code on `main` while building another source branch:

```bash
gh workflow run release.yml -R madebycli/Kiyori --ref main \
  -f source_ref=feature/kiyori-integrated-rebuild \
  -f tag=v1.6.0-rc1 \
  -f universal_only=true \
  -f prerelease=true \
  -f draft=true
```

Watch recent runs:

```bash
gh run list -R madebycli/Kiyori --limit 10
gh run watch -R madebycli/Kiyori
```

## Branch cleanup after verification

Delete `master` after CI and Debug APKs pass:

```bash
git push origin --delete master
```

Keep `recovery/phase0-backup` until the first signed release has been built, downloaded, signature-verified, and backed up. Then preserve it as a tag before deleting the branch:

```bash
git tag recovery-phase0-final origin/recovery/phase0-backup
git push origin recovery-phase0-final
git push origin --delete recovery/phase0-backup
```

Keep `develop` as the upstream comparison branch and keep `feature/kiyori-integrated-rebuild` as the active development branch.

## Before product development

The repository baseline is ready when:

- Android CI completes successfully;
- FOSS and GMS debug APKs are downloadable and installable;
- the `release` environment and its four signing secrets exist;
- a draft signed release completes and its signatures verify;
- `feature/kiyori-integrated-rebuild` points to the current `main` before the first implementation commit.

Branding and reconstructed product behavior are deliberately outside this baseline.
