> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# One Consolidated Owner Test, Continuous Automated Gates

## Philosophy

The owner does not need to install and approve four intermediate APKs again. The coding AI must still compile and test continuously.

## Internal automated gates

### After navigation core

```bash
./gradlew \
  :core:model:testDebugUnitTest \
  :core:domain:testDebugUnitTest \
  :app:testFossDebugUnitTest \
  :app:assembleFossDebug \
  --no-daemon --stacktrace
```

### After Calendar

```bash
./gradlew \
  :feature:calendar:testDebugUnitTest \
  :app:testFossDebugUnitTest \
  :app:assembleFossDebug \
  --no-daemon --stacktrace
```

### After shortcuts

```bash
./gradlew \
  :core:model:testDebugUnitTest \
  :core:domain:testDebugUnitTest \
  :app:testFossDebugUnitTest \
  :app:assembleFossDebug \
  --no-daemon --stacktrace
```

## Final CI matrix

```bash
./gradlew \
  :app:assembleFossDebug \
  :app:assembleGmsDebug \
  :app:testFossDebugUnitTest \
  :app:testGmsDebugUnitTest \
  :core:model:testDebugUnitTest \
  :core:domain:testDebugUnitTest \
  :feature:calendar:testDebugUnitTest \
  :wearos:assembleDebug \
  :app:lintFossDebug \
  :app:lintGmsDebug \
  --no-daemon \
  --stacktrace
```

Separately:

```bash
./gradlew :app:assembleFossRelease --no-daemon --stacktrace
```

The unsigned release candidate is not expected to install.

## Required tests

### Navigation model

- Home always present/visible;
- min/max visible;
- reset hides Profile;
- migration preserves visible Profile;
- legacy static IDs;
- v2 Calendar config;
- final typed config;
- unknown parameters;
- over-capacity repair;
- duplicate normalization;
- Season singleton;
- remove exact shortcut only;
- add hidden at capacity.

### Routes

- Calendar main vs nested;
- Season main vs nested;
- chart main vs nested;
- current-list main vs nested;
- Home account → nested own profile;
- hidden active tab → Home;
- startup → Home.

### Calendar

- page 0 = today;
- page 14 = today+14;
- Monday week;
- boundary arrows;
- swipe page/date synchronization;
- Sunday/Monday transition;
- bounds;
- timezone;
- DST;
- counts/content same filter;
- three filter states;
- list/grid persistence;
- process recreation restore;
- stale saved index clamped if today changed.

### Season

- CURRENT/NEXT;
- year/season boundaries;
- runtime label;
- ViewModel key includes season/year;
- resume refresh.

### Operational

- token backup exclusion;
- notification target package;
- phone shortcut module boundary;
- release/debug target resource;
- splash asset;
- Wear compile/target SDK.

## One owner device test

1. install/upgrade;
2. login;
3. change theme/accent;
4. Home notification/settings/account;
5. navigation editor;
6. add every shortcut category;
7. capacity behavior;
8. reorder/hide/remove;
9. check X alignment;
10. restart/persistence;
11. Calendar day tap;
12. Calendar arrows;
13. Calendar swipe across week boundary;
14. tri-state filter;
15. list/grid;
16. current/next Season;
17. nested Discover Season unchanged;
18. nested Home/chart views unchanged;
19. bottom bar;
20. wide rail if available;
21. splash;
22. Light/Dark/OLED.

Only issues found here require a correction APK.
