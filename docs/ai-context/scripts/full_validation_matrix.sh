#!/usr/bin/env bash
set -Eeuo pipefail
chmod +x ./gradlew
./gradlew \
  :app:assembleFossDebug \
  :app:assembleGmsDebug \
  :app:testFossDebugUnitTest \
  :app:testGmsDebugUnitTest \
  :core:model:testDebugUnitTest \
  :core:domain:testDebugUnitTest \
  :feature:calendar:testDebugUnitTest \
  :app:lintFossDebug \
  :app:lintGmsDebug \
  --no-daemon \
  --stacktrace
./gradlew \
  :app:assembleFossRelease \
  :app:assembleGmsRelease \
  --no-daemon \
  --stacktrace
