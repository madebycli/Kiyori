# Current State — Kiyori Plan 2

- Updated: 2026-08-05T14:47:00+02:00
- Repository: `madebycli/Kiyori`
- Integration branch: `feature/plan2-recovery-phase3-4`
- Draft PR: #6 to `main`
- Baseline `main`: `5216fb0eb8cb60404543306e2401e4d27833a2bc`
- Verified rescue source: `4e54026b3f83029d97c77354c8a2e8fe588914cb`
- Audited upstream `develop`: `7d49076ded4d93b35c94262147695c3889396d76`
- Rescue consolidation merge: `0d255324bc54cf89070e86f04bc8d57993375221`
- Upstream integration merge: `05b2e23756613e3f1984684fa219c67f7204403e`
- Last fully green pre-version source: `1811092adfe6fe28efe536eda26b092e4797d4b0`
- Version metadata: `1.6.0.1` / code `114`
- Expected tag: `v1.6.0.1` — not yet created

## Completed

- Re-audited current refs and created immutable dated backups for main, the prior integrated rebuild, and the APK rescue head.
- Consolidated the verified rescue product superset onto current main with a real merge commit and integrated the three missing audited upstream commits with preserved history.
- Implemented navigation configuration v4 and lossless migration for untouched and customized v3 states, five-tab layouts, Profile, Calendar, and typed shortcuts.
- Preserved the accepted Discover and character/team designs while adding responsive layouts, independent state handling, deterministic fallback, empty states, and focused tests.
- Hardened app lock around process lifecycle, strong system authentication, all timeout boundaries, initialization, cancellation/failure mapping, and exact-once pending destinations.
- Routed supported notifications through the same lock gate without coupling WorkManager background checks to app-lock state.
- Removed the Wear module, sources, bridge, callback, dependencies, version metadata, CI contracts, and release artifacts.
- Hardened the signed release workflow for canonical main, deterministic universal FOSS/GMS assets, explicit zipalign/apksigner, signing identity and Android metadata verification, collision rejection, and checksums.
- Added the English product changelog and Android changelog for version code 114.

## Verified build evidence

Android CI run `31005865364` / run number 140 succeeded on source
`1811092adfe6fe28efe536eda26b092e4797d4b0`.

Passed in one matrix:

- FOSS and GMS debug assembly;
- FOSS and GMS app unit tests;
- core model, domain, and UI tests;
- Calendar and media-details module tests;
- FOSS and GMS lint;
- minified FOSS and GMS release assembly with R8.

Artifact: `android-ci-140`, ID `8930765110`, GitHub digest
`sha256:339edd1603320dbdcf534a01ac49acc06042b0282a374eaa88fc4e84ff7dbbb7`.

## Current gate

Run the same complete matrix on the versioned/documented head. After a green result, the remaining
external acceptance is a real-device/upgrade pass, including App Lock, notification display/taps,
Deep Links, phone/landscape/tablet-width presentation, themes, accessibility, and EN/DE checks.

## Honest release state

There is no verified `v1.6.0.1` tag or published GitHub release yet. Signing secrets and a real device
must be available before the protected main-only release workflow can create and verify the exact three
assets. Do not report a published release until tag, signatures, metadata, checksums, asset set, and
GitHub release state are all independently confirmed.
