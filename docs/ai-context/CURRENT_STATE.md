# Current State — Kiyori

- Updated: 2026-08-04T21:55:51+02:00
- Repository: `madebycli/Kiyori`
- Branch: `feature/kiyori-integrated-rebuild`
- Last published product checkpoint: `02986688e755672415f6e7629c59592e2c36c294`
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
- Calendar date-range boundary behavior is covered by focused JVM unit tests. Public README,
  Fastlane changelog and release-candidate notes now describe Kiyori in English without choosing final metadata.

## Build status

Gradle 9.5.0 now runs under a workspace-local full Java 17 and Android SDK through the Work proxy.
The Work matrix reached and repaired all affected Kotlin modules. FOSS/GMS debug APKs, focused JVM
tests, Wear debug/lint, and FOSS/GMS app lint pass. Wear now compiles at API 37 to match the shared
module dependency metadata; its target SDK was not changed. Minified phone release validation remains
blocked before R8 because the Work cache lacks a release-only Maven artifact and its network guard
cancelled the required Maven request.

## Tests and checks

- Passed: `git diff --check`; resource source/manifest inspection; 512×512 PNG dimensions; protected Auth/API source content comparison.
- Passed: targeted auth/API reference comparison; no protected auth/API source files changed.
- Passed: `:core:model:testDebugUnitTest`, including the typed navigation repair invariants.
- Passed: `:core:ui:compileDebugKotlin`, `:feature:calendar:testDebugUnitTest`,
  `:feature:explore:compileDebugKotlin`, `:feature:home:compileDebugKotlin`, and
  `:feature:settings:compileDebugKotlin`.
- Passed: FOSS and GMS universal debug assembly, `:app:lintFossDebug`, `:app:lintGmsDebug`, and
  `:wearos:clean :wearos:assembleDebug :wearos:lintDebug`.
- Produced: FOSS universal debug APK SHA-256
  `01fcf6036914f8cab54a3e1bc40d792a58b0e7f2231ca1723cf41abc503b59ae`;
  GMS universal debug APK SHA-256
  `d5e78a25811b29897b973d5f466c9c2f003d468ae49ff8878137412d047e83c2`.

## Known blockers

- No local GitHub Git credential or GitHub CLI. Published commits use the connected GitHub integration and are synchronized back to the local feature branch.
- Release assembly cannot yet enter R8: `sh.calvin.reorderable:reorderable-android:3.1.0` is absent
  from the Work cache. Debug uses the distinct cached `reorderable-android-debug:3.1.0` artifact;
  it must not be substituted into a release build.
- The Work network guard cancelled both Gradle and direct Maven hydration for that exact release
  coordinate. This is an external environment permission blocker, not a source or repository failure.
- The master prompt names `00_USE_THIS_FILE.md` and `02_CHECKPOINT_POLICY.md`, but neither file exists in this checkout, its reachable history, or the provided upload. The explicit checkpoint rules in the master prompt are being followed.
- Read-only merge simulation reports a README conflict with the independent `main` commit `90898bfe`.
  Resolving it requires a merge or rebase against `main`, both explicitly prohibited for this campaign.

## Next exact action

Once Maven access is available, hydrate the exact release artifact and run
`:app:assembleFossRelease :app:assembleGmsRelease --no-daemon --max-workers=1 -Dkotlin.compiler.execution.strategy=in-process`;
then inspect unsigned release artifacts and complete
owner device acceptance. Do not create release metadata before those checks.

## 2026-08-04 continuation

Remote checkpoints through `02986688` repair the actual Kotlin findings in Calendar, dynamic top-level
hosts and navigation settings, then align Wear compile SDK with shared dependencies. No protected ref changed.
