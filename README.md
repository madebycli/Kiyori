# Kiyori

Kiyori is a phone-only Android client for discovering, planning, and tracking anime and manga with AniList.
It preserves the established AniList authentication and API contract while providing a focused,
configurable experience for phones, landscape layouts, and wider Android screens.

## Highlights

- Configurable main navigation with two to five destinations and one shared projection for the bottom bar and navigation rail.
- A version-4 default layout of Home, Anime, Manga, Explore, and Calendar, with lossless migration of custom version-3 layouts and shortcuts.
- A date-based Calendar for today through the next fourteen days, including accessible week controls, exact filtered day counts, and list/grid presentation.
- Responsive Discover actions that keep the existing chip design while using compact or wide multi-column layouts.
- Character and team browsing with role and voice-language filters, independent loading states, and deterministic voice-actor fallback.
- Optional strong app locking with biometric or device-credential authentication, configurable timeouts, and lock-safe deep-link and notification routing.
- Separate FOSS and GMS variants. The Android package is `app.kiyori` (`app.kiyori.debug` for debug builds).

## Build and validation

Kiyori uses the checked-in Gradle wrapper and JDK 17. The complete phone validation matrix is:

```bash
bash scripts/install-android-sdk.sh
bash docs/ai-context/scripts/full_validation_matrix.sh
```

The matrix assembles FOSS and GMS debug variants, runs app and module unit tests, runs both lint variants,
and assembles minified FOSS and GMS release candidates with R8. Signed universal release assets are
created only by the repository's protected **Signed GitHub Release** workflow from canonical `main`.

## Release status

Version metadata is `1.6.0.1` (`versionCode` 114). The complete pre-version matrix passed in Android CI
run 140 on commit `1811092adfe6fe28efe536eda26b092e4797d4b0`. The versioned candidate still requires its own green
CI run, device/upgrade acceptance, and verified release signing before tag `v1.6.0.1` may be published.
No tag or GitHub release should be assumed from this repository state alone.

## Privacy and security

Credential-bearing preferences are excluded from Android cloud backup and device-to-device transfer.
The app lock does not suppress WorkManager notification checks; notification, widget, and external-link
destinations are buffered behind the lock gate and consumed exactly once after successful authentication.
Never commit signing material, tokens, private keys, or local SDK configuration.
