# Current State — Kiyori

- Updated: 2026-08-04T21:16:33+02:00
- Repository: `madebycli/Kiyori`
- Branch: `feature/kiyori-integrated-rebuild`
- Last published product checkpoint: `5f9f7aad25e2168fe229dd5138d17428c6d990da`
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
The initial Kotlin compilation uncovered four source-level defects, all corrected in `5f9f7aad`.
The CI script explicitly covers Wear debug/lint and both minified phone release variants; its complete
execution is now the remaining release-readiness validation.

## Tests and checks

- Passed: `git diff --check`; resource source/manifest inspection; 512×512 PNG dimensions; protected Auth/API source content comparison.
- Passed: targeted auth/API reference comparison; no protected auth/API source files changed.
- Passed: `:core:model:testDebugUnitTest`, including the typed navigation repair invariants.
- Passed: `:core:ui:compileDebugKotlin` and `:feature:calendar:testDebugUnitTest`.

## Known blockers

- No local GitHub Git credential or GitHub CLI. Published commits use the connected GitHub integration and are synchronized back to the local feature branch.
- No product-source blocker is currently known. The remaining work is the complete FOSS/GMS/Wear/test/lint/R8 matrix,
  artifact inspection and owner device acceptance.
- The master prompt names `00_USE_THIS_FILE.md` and `02_CHECKPOINT_POLICY.md`, but neither file exists in this checkout, its reachable history, or the provided upload. The explicit checkpoint rules in the master prompt are being followed.
- Read-only merge simulation reports a README conflict with the independent `main` commit `90898bfe`.
  Resolving it requires a merge or rebase against `main`, both explicitly prohibited for this campaign.

## Next exact action

Run the full validation matrix from `docs/ai-context/scripts/full_validation_matrix.sh`, inspect the generated
FOSS debug APK and record its SHA-256. Do not create release metadata before the owner device acceptance.

## 2026-08-04 continuation

Remote checkpoint `5f9f7aad` fixes the first actual Kotlin validation findings: Android JUnit annotations in the
new tests, a valid non-data shortcut destination, and Compose-compatible Calendar scaffold/loading code.
