# Kiyori

Kiyori is an Android client for discovering, planning and tracking anime and manga with AniList.
It keeps AniList authentication and API compatibility while offering a focused, configurable phone experience.

## Highlights

- A configurable main navigation with two to five destinations, including Calendar and typed media shortcuts.
- A date-based Calendar for today through the next fourteen days, with accessible week controls,
  exact filtered day counts, list or grid presentation, and a compact list-status filter.
- Quick entry points for current lists, charts and seasonal discovery, plus direct access to
  notifications, settings and the signed-in account from Home.
- Separate FOSS and GMS variants. The Android package is `app.kiyori` (`app.kiyori.debug` for debug builds).

## Build and validation

Kiyori uses the checked-in Gradle wrapper and JDK 17. The full local/CI validation matrix is:

```bash
bash scripts/install-android-sdk.sh
bash docs/ai-context/scripts/full_validation_matrix.sh
```

The matrix builds FOSS and GMS debug variants, runs unit tests, runs lint, and assembles a minified
FOSS release candidate. Signed release builds are handled by the repository's **Signed GitHub Release**
workflow after the required release environment secrets and runtime acceptance are in place.

## Release status

The current integrated rebuild is a release candidate, not a published release. Version metadata is
intentionally finalized only after the complete validation matrix and device acceptance pass.

## Privacy and security

Kiyori preserves the established AniList OAuth and API contract. Credential-bearing preferences are
excluded from Android cloud backup and device-to-device transfer. Never commit signing material,
tokens, private keys, or local SDK configuration.
