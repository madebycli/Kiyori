# Recovery Checkpoint

- Updated: 2026-08-04T21:16:33+02:00
- Branch: `feature/kiyori-integrated-rebuild`
- Last synchronized remote HEAD: `5f9f7aad25e2168fe229dd5138d17428c6d990da`
- Upstream develop: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Current gate: Gate 5 / full validation matrix running
- Completed: Preflight, sensitive-file scan, auth/API contract scans, Draft PR #2, Kiyori identity and phone assets, variant-safe notification routing, typed navigation model/codec, normalized preferences persistence, shared resolver, navigation editor, typed current-list/chart/season projection, nested-profile Home action, date-based Calendar main host, bounded pager/DST tests, and DataStore backup exclusion.
- Build state: Gradle 9.5.0 runs under a workspace-local Java 17 JDK and Android SDK through the Work proxy.
  Targeted navigation and Calendar compilation/tests pass after source-level repairs.
- Passed checks: `git diff --check`; targeted protected auth/API source comparison; manifest/resource inspection;
  512×512 preview dimension check; `:core:model:testDebugUnitTest`; `:core:ui:compileDebugKotlin`;
  `:feature:calendar:testDebugUnitTest`.
- Failing command: none after `5f9f7aad`; the complete FOSS/GMS/Wear/test/lint/R8 matrix is pending.
- Uncommitted files: no product-source files in the remote tree.
- Next exact action: execute `docs/ai-context/scripts/full_validation_matrix.sh`, inspect the FOSS debug APK
  and document its SHA-256; do not create a release or version bump.
- Last successful remote publication: `5f9f7aad25e2168fe229dd5138d17428c6d990da`.
- Latest source publication: `b573abdda21191cee9d41e98b72a05cebc254e68` adds Wear and both release variants to CI.
- Read-only merge simulation against `main` reports one README conflict. Its resolution requires an
  explicitly authorized merge/rebase workflow, which remains prohibited by the campaign rules.
