# Current State — Kiyori

- Updated: 2026-08-04T18:10:00+02:00
- Repository: `madebycli/Kiyori`
- Branch: `feature/kiyori-integrated-rebuild`
- Last published product checkpoint: `3e6e5b6b0b400ceba7f9eacaf825c40f59e59903`
- Upstream `develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base with `origin/develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Protected refs verified unchanged: `main` `90898bfe`, `develop` `01a8a4ab`, `recovery/phase0-backup` `476ad447`

## Current gate

Gate 5 — stabilization and release-readiness verification.

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
- Calendar presentation is persisted independently: new Calendar tabs open in a detail-rich list,
  can switch to a grid, and keep the same date and tri-state list filter in either presentation.
- Dynamic current-list, chart, and season shortcuts now use main-destination route identities;
  their reused hosts omit the Back action while original nested routes keep it.
- The navigation state registers the resolved dynamic routes as top-level back stacks, so shortcut
  tabs are selectable in Bottom Bar and Rail instead of being appended to Home's nested stack.
- Added unit coverage for default/migrated configuration, duplicate shortcut repair, Home protection
  and the visible-tab capacity. Dynamic shortcut icons now identify their list/chart category.
- The Calendar week header now shows exact per-day filtered counts, disables unavailable dates and
  boundary arrows, announces each count to accessibility services, and renders a thin accent line
  for the selected day. Count queries use the same local-day/DST bounds and tri-state filter as the pager.

## Build status

Gradle 9.5.0 has been downloaded and starts with Java 17 plus a workspace temporary directory.
The wrapper reaches project configuration; the remaining validation blocker is that the Work runtime
terminates the local curl-backed Maven proxy before Gradle can finish hydrating uncached artifacts.

## Tests and checks

- Passed: `git diff --check`; resource source/manifest inspection; 512×512 PNG dimensions; protected Auth/API source content comparison.
- Passed: targeted auth/API reference comparison; no protected auth/API source files changed.
- Attempted: `:feature:calendar:compileFossDebugKotlin --no-daemon --stacktrace`; source compilation
  is not reached because uncached Android Gradle Plugin artifacts lose their localhost proxy mid-resolution.

## Known blockers

- No local GitHub Git credential or GitHub CLI. Published commits use the connected GitHub integration and are synchronized back to the local feature branch.
- The Java process cannot resolve public Maven hostnames in this Work container even though curl can;
  the ephemeral process model also prevents a local curl-backed Maven proxy from surviving full dependency hydration.
- The master prompt names `00_USE_THIS_FILE.md` and `02_CHECKPOINT_POLICY.md`, but neither file exists in this checkout, its reachable history, or the provided upload. The explicit checkpoint rules in the master prompt are being followed.

## Next exact action

In an environment with the Gradle 9.5.0 wrapper distribution available, run:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \\
LD_LIBRARY_PATH=/usr/lib/jvm/java-17-openjdk-amd64/lib \\
PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:$PATH \\
./gradlew :app:assembleFossDebug --no-daemon --stacktrace
```

Run the CI validation matrix on PR #2 or use a persistent Maven proxy, then complete the release matrix.

## 2026-08-04 continuation

Remote checkpoint `0da3b861` adds the editor, typed shortcut projection, Home actions, a separate date-based Calendar main host and DataStore backup exclusion. The source is still blocked from Gradle validation by the unavailable Gradle 9.5.0 distribution; Calendar list/grid persistence and the final stabilization matrix remain.
