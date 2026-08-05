# Plan 2 Recovery / Phase 3 / Phase 4 Handoff

## Repository and history

Repository: `madebycli/Kiyori`

Integration branch: `feature/plan2-recovery-phase3-4`

Draft PR: #6, targeting `main`

Verified source points:

- main baseline: `5216fb0eb8cb60404543306e2401e4d27833a2bc`
- prior integrated rebuild: `36de346b31d22e0e566601393210e8cb7b423fcd`
- complete APK rescue: `4e54026b3f83029d97c77354c8a2e8fe588914cb`
- audited upstream develop: `7d49076ded4d93b35c94262147695c3889396d76`
- rescue merge: `0d255324bc54cf89070e86f04bc8d57993375221`
- upstream merge: `05b2e23756613e3f1984684fa219c67f7204403e`
- last fully green pre-version source: `1811092adfe6fe28efe536eda26b092e4797d4b0`

Remote backups:

- `backup/2026-08-05-main-5216fb0`
- `backup/2026-08-05-integrated-rebuild-36de346`
- `backup/2026-08-05-apk-feature-rescue-4e54026`

No force-push was used. No Kiyori product changes were written to `develop`.

## Phase 2R repairs

Character/team:

- accepted visual structure retained;
- Characters remains the default section;
- role and voice-language filters retained;
- canonical case-insensitive language deduplication;
- deterministic selected-language fallback and no-voice-actor fallback;
- independent Character and Team loading/empty states;
- long text constrained with ellipsis;
- focused JVM tests added.

App Lock:

- default remains off;
- activation/deactivation persist only after successful system authentication;
- strong biometric or device credential;
- process-lifecycle foreground/background timing;
- immediate, 1, 5, 15, and 30 minute boundaries tested;
- cold start and initialization block protected content;
- rotation, internal navigation, and prompt activity do not create false relocks;
- cancel, lockout, unavailable hardware/enrollment, and unknown errors mapped;
- widget, external, and notification destinations buffered and consumed exactly once after unlock;
- WorkManager notification generation remains independent of lock state;
- supported media/activity/thread/user notification taps use the shared lock gate.

## Phase 3

- Discover action chips use 2/3/4-column responsive layout while retaining the accepted chip design.
- The duplicate Calendar Discover action is removed; lower preview content remains.
- Main navigation schema is version 4.
- New/untouched defaults: Home, Anime, Manga, Explore, Calendar.
- Profile remains configurable but hidden by default.
- Untouched v3 defaults upgrade to v4; custom order, visibility, Profile, Calendar, five-tab states, and typed shortcuts are preserved.
- Bottom Bar and Rail use the same resolver and route universe.

## Phone-only proof

Removed from the active tree and contracts:

- `wearos` module and all sources;
- Settings include;
- app dependency;
- phone-to-Wear authentication bridge and callback;
- GMS Wear launcher source;
- Wear libraries and aliases;
- Wear version metadata;
- Wear CI and release outputs.

The internal Kotlin namespace remains `com.axiel7.anihyou`. Application IDs remain `app.kiyori` and
`app.kiyori.debug`.

## Validation evidence

Android CI run `31005865364` / run number 140 passed on
`1811092adfe6fe28efe536eda26b092e4797d4b0`.

The single matrix passed:

- FOSS/GMS debug assembly;
- FOSS/GMS app unit tests;
- core model/domain/UI tests;
- Calendar and media-details tests;
- FOSS/GMS lint;
- minified FOSS/GMS release assembly and R8.

Artifact evidence:

- name: `android-ci-140`
- artifact ID: `8930765110`
- GitHub artifact digest: `sha256:339edd1603320dbdcf534a01ac49acc06042b0282a374eaa88fc4e84ff7dbbb7`

## Version and release contract

- version name: `1.6.0.1`
- version code: `114`
- intended tag: `v1.6.0.1`
- expected exact assets:
  - `Kiyori-1.6.0.1-foss-universal.apk`
  - `Kiyori-1.6.0.1-gms-universal.apk`
  - `SHA256SUMS.txt`

The protected release workflow accepts canonical `main` only, rejects tag/release collisions, runs the
full matrix, explicitly zipaligns and signs both universal APKs, verifies certificate identity,
application ID, version name/code, and exact assets, and creates a draft unless publication is explicitly requested.

## Remaining genuine risks / external gates

- The versioned/documented head must pass its final full CI matrix.
- Real-device and upgrade acceptance is not replaceable by JVM/CI evidence. It must cover App Lock,
  system notifications and taps, deep links, phone/landscape/tablet-width layouts, Light/Dark/OLED,
  accessibility, and EN/DE.
- Protected release signing secrets and the expected signing certificate must be available to the main-only workflow.
- No `v1.6.0.1` tag or GitHub release is verified at this checkpoint. Never report publication until the tag,
  signatures, metadata, checksums, exact asset set, and GitHub release state have all been confirmed.
