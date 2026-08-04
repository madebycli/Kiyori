# Upstream, Build, Authentication and Release Boundaries

## Verified bootstrap state

- Backup HEAD: `476ad447217ecae2b7c7ae710f7981ca55d9a003`
- Backup upstream base: `259e81de6cd3ea51a488849bbd4777a2c3c7f342`
- Current repository `develop`: `01a8a4abe98c778d1015a33072a11efdb4ef8593`

Reverify these before implementation because upstream may move.

## Build posture

Prefer current upstream Gradle and action versions. Port the old NixOS shell, CI and signing workflow by intent, not by blind full-file replacement.

Expected final matrix:

```text
:app:assembleFossDebug
:app:assembleGmsDebug
:app:testFossDebugUnitTest
:app:testGmsDebugUnitTest
:core:model:testDebugUnitTest
:core:domain:testDebugUnitTest
:feature:calendar:testDebugUnitTest
:wearos:assembleDebug
:app:lintFossDebug
:app:lintGmsDebug
:app:assembleFossRelease
```

The unsigned release candidate validates R8/resources and is not expected to install.

## Signing secrets

- `KEYSTORE_FILE`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Never log or commit values. The first distributed production key becomes permanent for future upgrades.
