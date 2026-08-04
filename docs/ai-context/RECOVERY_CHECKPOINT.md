# Recovery Checkpoint

- Updated: 2026-08-04T17:51:43+02:00
- Branch: `feature/kiyori-integrated-rebuild`
- Last synchronized remote HEAD: `c56ccd90f9a9295f6638d39a56fe2c37cf7f6e7d`
- Upstream develop: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Current gate: Gate 4 / typed shortcut hosts and Calendar presentation persistence complete; tests and stabilization pending
- Completed: Preflight, sensitive-file scan, auth/API contract scans, Draft PR #2, Kiyori identity and phone assets, variant-safe notification routing, typed navigation model/codec, normalized preferences persistence, shared resolver, navigation editor, typed current-list/chart/season projection, nested-profile Home action, date-based Calendar main host, bounded pager/DST tests, and DataStore backup exclusion.
- Build state: Gradle 9.5.0 is locally available and starts under Java 17 with the JDK library path
  and a workspace temporary directory. JVM Maven DNS fails in Work, so a local curl-backed mirror
  is hydrating dependencies before compilation.
- Passed checks: `git diff --check`; targeted protected auth/API source comparison; manifest/resource inspection; 512×512 preview dimension check.
- Failing command: `:app:assembleFossDebug --no-daemon --stacktrace` while the dependency mirror cache is incomplete.
- Exact error summary: Gradle's JVM reports `Temporary failure in name resolution` for public Maven
  hosts although curl can reach them; the wrapper itself is verified.
- Uncommitted files: none in the remote tree; local clone index is intentionally behind published
  connector commits and is used only as a content workspace.
- Next exact action: complete dependency hydration, compile the changed modules, then add focused
  Calendar, navigation migration and route tests.
- Last successful remote publication: `c56ccd90f9a9295f6638d39a56fe2c37cf7f6e7d`.
