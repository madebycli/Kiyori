# Current State — Kiyori

- Updated: 2026-08-04T04:35:00+02:00
- Repository: `madebycli/Kiyori`
- Branch: `feature/kiyori-integrated-rebuild`
- Local/remote checkpoint before this context update: `af5bef6b2a0ae69cb2dda69da8087d6e9a467411`
- Upstream `develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Merge-base with `origin/develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Protected refs verified unchanged: `main` `90898bfe`, `develop` `01a8a4ab`, `recovery/phase0-backup` `476ad447`

## Current gate

Gate 1 — Kiyori branding, part 1: application identity and visible labels.

## Completed

- Preflight completed and Draft PR #2 opened against `main`.
- Release application ID changed to `app.kiyori`; debug resolves to `app.kiyori.debug` through the existing suffix.
- Internal Kotlin namespace remains `com.axiel7.anihyou`.
- Visible release/debug labels changed to `Kiyori` / `Kiyori Debug`.
- Auth/API/OAuth contract scan completed before the branding edit; none of its files changed.

## Build status

Not yet validated by Gradle. Java 17 runs when `LD_LIBRARY_PATH` includes its JDK library directory, but the Gradle wrapper cannot download Gradle 9.5.0 because this Work environment has no route to `services.gradle.org`.

## Tests and checks

- Passed: `git diff --check`.
- Passed: targeted auth/API reference comparison; no protected auth/API source files changed.
- Blocked: `:app:assembleFossDebug --no-daemon --stacktrace` before task configuration because Gradle 9.5.0 cannot be downloaded.

## Known blockers

- No local GitHub Git credential or GitHub CLI. Published commits use the connected GitHub integration and are synchronized back to the local feature branch.
- Gradle wrapper distribution is absent from the cache and network access to its upstream download is unavailable.
- The master prompt names `00_USE_THIS_FILE.md` and `02_CHECKPOINT_POLICY.md`, but neither file exists in this checkout, its reachable history, or the provided upload. The explicit checkpoint rules in the master prompt are being followed.

## Next exact action

In an environment with the Gradle 9.5.0 wrapper distribution available, run:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 \\
LD_LIBRARY_PATH=/usr/lib/jvm/java-17-openjdk-amd64/lib \\
PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:$PATH \\
./gradlew :app:assembleFossDebug --no-daemon --stacktrace
```

On success, continue Gate 1 with the Kiyori vector/adaptive launcher assets, splash mark, notification icon, SVG source, preview, and design/originality note.
