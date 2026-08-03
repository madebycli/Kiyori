# Kiyori development baseline

This document describes the repository baseline before product reconstruction and branding work.

## Branch policy

| Branch | Purpose | Keep? |
|---|---|---|
| `main` | Default branch, verified repository baseline, CI and release workflows | Yes |
| `feature/kiyori-integrated-rebuild` | Active product reconstruction work | Yes |
| `develop` | Unmodified upstream comparison and update source | Yes, while upstream syncing is needed |
| `recovery/phase0-backup` | Historical recovery snapshot | Keep until the first verified signed Kiyori release, then tag/archive and delete the branch |
| `master` | Superseded historical default and transfer scaffolding | Safe to delete now |

Development work belongs on `feature/kiyori-integrated-rebuild`. Open pull requests from that branch into `main`. Do not implement new Kiyori features directly on `develop`.

## Workflows

### Android CI

File: `.github/workflows/android-ci.yml`

Runs on pushes to `main`, pull requests into `main`, and manual dispatch. It builds FOSS and GMS debug variants, runs unit tests and lint, builds Wear OS debug, and compiles a FOSS release variant without publishing it.

### Debug APKs

File: `.github/workflows/debug-apks.yml`

Manual workflow. Select any branch in the GitHub **Run workflow** menu and choose `all`, `foss`, or `gms`. The produced debug APKs and `SHA256SUMS.txt` are available as a workflow artifact.

Debug APKs use Android's generated debug signing key. They are for installation and testing, not distribution.

### Signed GitHub Release

File: `.github/workflows/release.yml`

Manual workflow. Select the branch in GitHub, optionally enter a tag and title, and choose draft/prerelease behavior. If the tag is blank, the workflow uses `v<name>` from `version.properties`.

The workflow:

1. verifies that the tag does not already exist;
2. verifies all signing secrets;
3. builds signed FOSS and GMS release APKs;
4. verifies every APK with `apksigner`;
5. generates `SHA256SUMS.txt`;
6. creates a GitHub Release and uploads the APKs and checksums.

A release from a branch other than `main` is always marked as a prerelease. Draft mode is enabled by default.

Required repository secrets:

- `KEYSTORE_FILE`: base64-encoded PKCS#12/JKS keystore
- `KEYSTORE_PASSWORD`: keystore password
- `KEY_ALIAS`: signing key alias
- `KEY_PASSWORD`: signing key password

### Crowdin Sync

File: `.github/workflows/crowdin.yml`

Runs only in `madebycli/Kiyori`, only from `main`, and only when the source strings file changes, plus manual dispatch. It does not execute for fork pull requests or arbitrary feature-branch pushes.

Required secrets:

- `GH_TOKEN`
- `CROWDIN_PROJECT_ID`
- `CROWDIN_PERSONAL_TOKEN`

## Create and upload signing keys on NixOS

Enter a temporary shell with the required tools:

```bash
nix-shell -p git gh jdk17_headless coreutils gnugrep
```

Authenticate GitHub CLI if necessary:

```bash
gh auth login
```

Clone the repository or enter an existing checkout, then run:

```bash
bash scripts/setup-github-signing.sh madebycli/Kiyori
```

The script asks for the alias, certificate identity, and a password. It generates:

- `~/.local/share/kiyori-signing/kiyori-release.p12`
- `~/.local/share/kiyori-signing/kiyori-release-certificate.pem`

It uploads the four GitHub Actions secrets and prints SHA-1/SHA-256 certificate fingerprints.

Back up the `.p12` file and its password in at least two encrypted offline locations. Losing the signing key prevents future APK updates under the same application identity. Never add a keystore to Git, chat, email, or a GitHub Release.

Verify that the secret names exist:

```bash
gh secret list -R madebycli/Kiyori
```

GitHub never shows secret values after upload.

## Manual commands

Run the debug workflow from the CLI:

```bash
gh workflow run debug-apks.yml -R madebycli/Kiyori --ref main -f variant=all
```

Run a draft signed release from the CLI:

```bash
gh workflow run release.yml -R madebycli/Kiyori --ref main \
  -f tag=v1.6.0 \
  -f universal_only=true \
  -f prerelease=false \
  -f draft=true
```

Watch recent runs:

```bash
gh run list -R madebycli/Kiyori --limit 10
gh run watch -R madebycli/Kiyori
```

## Before product development

The repository baseline is ready when:

- Android CI completes successfully;
- Debug APKs are downloadable and installable;
- signing secrets exist;
- a draft signed release completes and its signatures verify;
- `feature/kiyori-integrated-rebuild` points to the current `main` before the first implementation commit.

Branding and reconstructed product behavior are deliberately outside this baseline.
