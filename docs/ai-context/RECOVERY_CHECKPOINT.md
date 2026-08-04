# Recovery Checkpoint

- Updated: 2026-08-04T18:10:00+02:00
- Branch: `feature/kiyori-integrated-rebuild`
- Last synchronized remote HEAD: `3e6e5b6b0b400ceba7f9eacaf825c40f59e59903`
- Upstream develop: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Current gate: Gate 5 / product gates complete; validation matrix and release readiness pending
- Completed: Preflight, sensitive-file scan, auth/API contract scans, Draft PR #2, Kiyori identity and phone assets, variant-safe notification routing, typed navigation model/codec, normalized preferences persistence, shared resolver, navigation editor, typed current-list/chart/season projection, nested-profile Home action, date-based Calendar main host, bounded pager/DST tests, and DataStore backup exclusion.
- Build state: Gradle 9.5.0 is locally available and starts under Java 17 with the JDK library path
  and a workspace temporary directory. The Calendar compile task reaches project configuration; JVM
  Maven DNS fails and the Work runtime terminates the temporary curl-backed proxy before hydration completes.
- Passed checks: `git diff --check`; targeted protected auth/API source comparison; manifest/resource inspection; 512×512 preview dimension check.
- Failing command: `:feature:calendar:compileFossDebugKotlin --no-daemon --stacktrace` while the
  dependency mirror cache is incomplete.
- Exact error summary: Gradle's JVM reports `Temporary failure in name resolution` for public Maven
  hosts although curl can reach them; the wrapper is verified, but its temporary localhost mirror
  is no longer reachable once the Work command process ends.
- Uncommitted files: none in the remote tree; local clone index is intentionally behind published
  connector commits and is used only as a content workspace.
- Next exact action: run the CI validation matrix on PR #2 or compile with a persistent Maven proxy,
  then inspect artifacts and complete the release-readiness gate.
- Last successful remote publication: `3e6e5b6b0b400ceba7f9eacaf825c40f59e59903`.
