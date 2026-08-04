# Current State — Kiyori

- Updated: 2026-08-04T11:25:59+02:00
- Repository: `madebycli/Kiyori`
- Branch: `feature/kiyori-integrated-rebuild`
- Last published product checkpoint: `eb7e0d9ba2ccbbfebc0892e203a72640a32ac2fa`
- Upstream `develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base with `origin/develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Protected refs verified unchanged: `main` `90898bfe`, `develop` `01a8a4ab`, `recovery/phase0-backup` `476ad447`

## Current gate

Gate 2 — shared app-shell resolver complete; navigation editor and dynamic shortcut hosts remain.

## Completed

- Preflight completed and Draft PR #2 opened against `main`.
- Release application ID changed to `app.kiyori`; debug resolves to `app.kiyori.debug` through the existing suffix.
- Internal Kotlin namespace remains `com.axiel7.anihyou`.
- Visible release/debug labels changed to `Kiyori` / `Kiyori Debug`.
- Added original Kiyori launcher resources in the phone app module: adaptive foreground/background,
  API-26 monochrome layer, legacy vector fallback, dedicated transparent splash mark, editable SVG
  source, and 512×512 PNG preview.
- The application manifest now uses the Kiyori icon and splash theme; shared Wear resources were not changed.
- Added a monochrome Kiyori notification icon with a runtime resource lookup and installed-variant package routing.
- Auth/API/OAuth contract scan completed before the branding edit; none of its files changed.
- Added the versioned typed main-navigation model and compact codec. It migrates legacy and v2
  static configurations, preserves a previously-visible Profile, and normalizes unknown, duplicate,
  invalid, over-capacity and missing-Home configurations without persisting Compose routes.
- Added the shared preferences flow and explicit rewrite migration for the schema. Both always encode
  a normalized v3 value and are ready to drive one app-shell resolver.
- The app now rewrites legacy navigation preferences on initialization and drives both the compact
  bottom bar and wide navigation rail from the same normalized resolver. Hidden active tabs are
  redirected to mandatory Home; Calendar is available as a configurable main destination.

## Build status

Not yet validated by Gradle. Java 17 runs when `LD_LIBRARY_PATH` includes its JDK library directory, but the Gradle wrapper cannot download Gradle 9.5.0 because this Work environment has no route to `services.gradle.org`.

## Tests and checks

- Passed: `git diff --check`; resource source/manifest inspection; 512×512 PNG dimensions; protected Auth/API source content comparison.
- Passed: targeted auth/API reference comparison; no protected auth/API source files changed.
- Blocked: `:app:assembleFossDebug --no-daemon --stacktrace` before task configuration because Gradle 9.5.0 cannot be downloaded.

## Known blockers

- No local GitHub Git credential or GitHub CLI. Published commits use the connected GitHub integration and are synchronized back to the local feature branch.
- Gradle wrapper distribution is absent from the cache and network access to its upstream download is unavailable.
- The master prompt names `00_USE_THIS_FILE.md` and `02_CHECKPOINT_POLICY.md`, but neither file exists in this checkout, its reachable history, or the provided upload. The explicit checkpoint rules in the master prompt are being followed.

## Next exact action

In an environment with the Gradle 9.5.0 wrapper distribution available, run:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \\
LD_LIBRARY_PATH=/usr/lib/jvm/java-17-openjdk-amd64/lib \\
PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:$PATH \\
./gradlew :app:assembleFossDebug --no-daemon --stacktrace
```

Implement the settings editor and typed dynamic shortcut hosts, then add model and route tests.

## 2026-08-04 continuation

Remote checkpoint `0da3b861` adds the editor, typed shortcut projection, Home actions, a separate date-based Calendar main host and DataStore backup exclusion. The source is still blocked from Gradle validation by the unavailable Gradle 9.5.0 distribution; Calendar list/grid persistence and the final stabilization matrix remain.
