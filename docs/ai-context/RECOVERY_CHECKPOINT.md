# Recovery Checkpoint — Plan 2 Phase 3/4

- Updated: 2026-08-05T14:47:00+02:00
- Active branch: `feature/plan2-recovery-phase3-4`
- Draft PR: #6 to `main`
- Protected baseline main: `5216fb0eb8cb60404543306e2401e4d27833a2bc`
- Rescue head retained: `4e54026b3f83029d97c77354c8a2e8fe588914cb`
- Current upstream develop retained: `7d49076ded4d93b35c94262147695c3889396d76`
- Last green pre-version source: `1811092adfe6fe28efe536eda26b092e4797d4b0`
- Version: `1.6.0.1` / code 114
- Current gate: final versioned CI, then external device/signing acceptance

## Remote safety refs

- `backup/2026-08-05-main-5216fb0`
- `backup/2026-08-05-integrated-rebuild-36de346`
- `backup/2026-08-05-apk-feature-rescue-4e54026`

No force-push was used. No Kiyori product commit was written to `develop`. The rescue and upstream
histories are retained through real merge commits.

## Product checkpoint

- Phase 2R character/team and App Lock audits are implemented and covered by focused tests.
- Phase 3 Discover responsiveness and navigation v4 migration are implemented.
- Phase 4 is phone-only; Wear sources and contracts are absent.
- Release workflow is main-only and verifies deterministic signed universal assets.
- English changelog and version-code-114 metadata are present.

## Build checkpoint

CI run `31005865364` (Android CI #140) passed the complete pre-version matrix on
`1811092adfe6fe28efe536eda26b092e4797d4b0` and uploaded artifact `android-ci-140`
(ID `8930765110`, digest `sha256:339edd1603320dbdcf534a01ac49acc06042b0282a374eaa88fc4e84ff7dbbb7`).

## Next exact action

Wait for the complete matrix on the versioned/documented head. If green, perform real-device and upgrade
acceptance. Only then merge the green PR to main and run the protected Signed GitHub Release workflow.
If a device or signing secret is unavailable, retain the verified candidate and report that single
external blocker; do not create or claim a public release.
