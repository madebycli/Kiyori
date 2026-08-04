# Recovery Checkpoint

- Updated: 2026-08-04T10:27:14+02:00
- Branch: `feature/kiyori-integrated-rebuild`
- Last synchronized remote HEAD: `44b56f02b9fea8f20c15adf6b4612c23b55608b4`
- Upstream develop: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Current gate: Gate 2 / typed configurable navigation / models and codec complete locally, publication pending
- Completed: Preflight, sensitive-file scan, auth/API contract scans, remote checkpoint publication, Draft PR #2, `app.kiyori` release ID, existing `.debug` suffix, Kiyori labels, original phone launcher/splash assets, Kiyori notification mark, installed-variant notification routing, and typed navigation model/codec.
- Build state: Source validation is blocked before Gradle configuration because the wrapper needs Gradle 9.5.0 and this environment cannot reach `services.gradle.org`. Java 17 itself succeeds with `LD_LIBRARY_PATH=/usr/lib/jvm/java-17-openjdk-amd64/lib`.
- Passed checks: `git diff --check`; targeted protected auth/API source comparison; manifest/resource inspection; 512×512 preview dimension check.
- Failing command: `./gradlew :app:assembleFossDebug --no-daemon --stacktrace`
- Exact error summary: `Downloading https://services.gradle.org/distributions/gradle-9.5.0-bin.zip` then `java.net.SocketException: Network is unreachable`.
- Uncommitted files: Gate 2 model/codec and its context updates only.
- Next exact action: commit and publish this Gate 2 checkpoint; then add a `main_navigation_config` DataStore flow that always exposes the normalized codec result.
- Last successful remote publication: `44b56f02b9fea8f20c15adf6b4612c23b55608b4`.
