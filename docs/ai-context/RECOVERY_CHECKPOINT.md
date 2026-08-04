# Recovery Checkpoint

- Updated: 2026-08-04T21:55:51+02:00
- Branch: `feature/kiyori-integrated-rebuild`
- Last synchronized product remote HEAD: `02986688e755672415f6e7629c59592e2c36c294`
- Upstream develop: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Current gate: Gate 5 / release-only Maven hydration pending
- Completed: Preflight, sensitive-file scan, auth/API contract scans, Draft PR #2, Kiyori identity and phone assets, variant-safe notification routing, typed navigation model/codec, normalized preferences persistence, shared resolver, navigation editor, typed current-list/chart/season projection, nested-profile Home action, date-based Calendar main host, bounded pager/DST tests, and DataStore backup exclusion.
- Build state: Gradle 9.5.0 runs under a workspace-local Java 17 JDK and Android SDK through the Work proxy.
  FOSS/GMS debug builds, focused JVM tests, FOSS/GMS lint, and Wear debug/lint pass after source-level repairs.
- Passed checks: `git diff --check`; targeted protected auth/API source comparison; manifest/resource inspection;
  512×512 preview dimension check; `:core:model:testDebugUnitTest`; `:core:ui:compileDebugKotlin`;
  `:feature:calendar:testDebugUnitTest`; `:feature:explore:compileDebugKotlin`;
  `:feature:home:compileDebugKotlin`; `:feature:settings:compileDebugKotlin`; FOSS/GMS debug assembly;
  `:app:lintFossDebug`; `:app:lintGmsDebug`; `:wearos:clean :wearos:assembleDebug :wearos:lintDebug`.
- Failing command: `:app:assembleFossRelease :app:assembleGmsRelease` stops before R8 because
  `sh.calvin.reorderable:reorderable-android:3.1.0` is not cached. Work cancelled the required Maven request;
  do not substitute its debug-only sibling artifact.
- Uncommitted files: no product-source files in the remote tree.
  FOSS universal debug SHA-256: `01fcf6036914f8cab54a3e1bc40d792a58b0e7f2231ca1723cf41abc503b59ae`.
- Next exact action: after Maven access is available, hydrate the exact release coordinate and rerun
  `:app:assembleFossRelease :app:assembleGmsRelease --no-daemon --max-workers=1 -Dkotlin.compiler.execution.strategy=in-process`;
  inspect artifacts, then obtain owner device acceptance.
- Last successful product remote publication: `02986688e755672415f6e7629c59592e2c36c294`.
- Latest CI source publication: `b573abdda21191cee9d41e98b72a05cebc254e68` adds Wear and both release variants to CI.
- Read-only merge simulation against `main` reports one README conflict. Its resolution requires an
  explicitly authorized merge/rebase workflow, which remains prohibited by the campaign rules.
