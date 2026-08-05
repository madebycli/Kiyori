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
  :core:ui:testDebugUnitTest \
  :feature:calendar:testDebugUnitTest \
  :feature:mediadetails:testDebugUnitTest \
  :app:lintFossDebug \
  :app:lintGmsDebug \
  --no-daemon \
  --stacktrace
./gradlew \
  :app:assembleFossRelease \
  :app:assembleGmsRelease \
  --no-daemon \
  --stacktrace
