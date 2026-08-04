# Kiyori Speedrun Reconstruction — All in One

> Generated from the authoritative split files in this repository. The source backup is Phase 0; reconstructed snippets are guidance, not exact lost source.


---

<!-- BEGIN reconstruction/00_START_HERE.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Kiyori Speedrun Recovery v2

## Mission

Rebuild the accepted Kiyori product from the surviving **pre-Phase-1 source backup**, while using the previous implementation experience to avoid repeating discovery work and known mistakes.

This package is written for a coding AI. It contains:

- the exact recovered baseline classification;
- an integrated implementation strategy;
- historical behavior and class/path knowledge;
- reconstructed Kotlin-oriented blueprints;
- known failures and their fixes;
- visual acceptance requirements;
- a consolidated final device test plan;
- optional APK forensics;
- release reconstruction.

## Critical truth

The surviving source ZIP is:

```text
Recovery Phase / Phase 0
branch: recovery/phase0-backup
HEAD: 476ad447217ecae2b7c7ae710f7981ca55d9a003
base upstream: 259e81de6cd3ea51a488849bbd4777a2c3c7f342
version: 1.6.0 / code 112
```

It contains build, NixOS, CI, Crowdin, and signing preparation only. It does not contain the accepted configurable navigation, date Calendar, shortcut registry, Phase-4 hardening, or final Kiyori branding.

## Speedrun strategy

Do not repeat four manual owner-test cycles.

Use:

- one integrated implementation branch;
- small internal commits grouped by vertical slice;
- automated compile/test gates after each slice;
- one installable FOSS debug APK for owner testing at the end;
- one correction pass if required;
- then release preparation.

This is not permission for one giant untested commit. The coding AI must keep the branch buildable internally.

## Evidence labels

- **Historical fact** — recorded from the prior implementation and CI/manual acceptance.
- **Screenshot fact** — visible in the supplied owner screenshots.
- **Reconstructed blueprint** — inferred from recorded classes, routes, tests, and fixes; not byte-identical lost source.
- **New v2 improvement** — intentionally better than the old accepted build, such as swipe-between-days in Calendar.

## Required reading order

1. `01_AI_BOOTSTRAP_SPEEDRUN_PROMPT.md`
2. `02_SPEEDRUN_MASTER_PLAN.md`
3. `03_RECOVERED_IMPLEMENTATION_MAP.md`
4. `04_KNOWN_FAILURES_AND_PREVENTION.md`
5. `05_UI_UX_CONTRACT_AND_IMPROVEMENTS.md`
6. `06_TEST_ONCE_FINAL_MATRIX.md`
7. `07_BRANCH_UPSTREAM_AND_COMMIT_STRATEGY.md`
8. `08_APK_FORENSICS_GUIDE.md`
9. `09_RELEASE_RECONSTRUCTION.md`
10. files under `blueprints/`

`SPEEDRUN_ALL_IN_ONE.md` contains the full textual context in one file.

## Non-negotiable honesty

Never claim reconstructed snippets are the exact deleted source. They are implementation-ready guidance that must be adapted to the current upstream tree and compiled.


<!-- END reconstruction/00_START_HERE.md -->


---

<!-- BEGIN reconstruction/01_AI_BOOTSTRAP_SPEEDRUN_PROMPT.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Senior-to-Coding-AI Bootstrap Prompt

Copy everything below into the coding AI session after the new GitHub repository exists and both source branches are available.

---

You are the implementation engineer rebuilding **Kiyori**, an Android AniList client fork.

The previous remote repository was lost. You have:

1. a new GitHub repository;
2. a branch containing the newest verified upstream `axiel7/AniHyou-android:develop`;
3. a preserved backup branch containing the old Phase-0 infrastructure baseline originally prepared under the former project name;
4. this complete `Kiyori-Speedrun-Recovery` package;
5. owner screenshots of the accepted later UI;
6. optionally one or more owner-built APKs.

## Your role

Act as a senior Android/Kotlin/Compose engineer. Do not re-run product discovery. The behavior is already specified.

Rebuild the accepted functionality in one integrated campaign, using small internal commits and automated gates. The owner will perform one consolidated device test near the end rather than testing after every historical phase.

## First actions

1. Read every Markdown file in this package.
2. Read all files under `blueprints/`.
3. Inspect the current newest upstream branch and the Phase-0 backup branch.
4. Record exact SHAs.
5. Confirm whether upstream moved since historical base `259e81de...`.
6. Create a feature branch from the **newest verified upstream develop**, not from the old backup.
7. Preserve the backup branch unchanged as reference.
8. Port only useful Phase-0 infrastructure after comparing it with current upstream.

Recommended branches:

```text
develop
  exact current upstream mirror

recovery/phase0-backup
  immutable imported backup at 476ad447...

main
  Kiyori product branch

feature/kiyori-integrated-rebuild
  integrated speedrun branch
```

When `main` does not yet exist, initialize it from current `develop`, then branch from `main`.

## Why current upstream is the base

The old backup predates all product phases. Starting from current upstream avoids rebuilding on stale APIs, GraphQL schema, Compose versions, resources, and Gradle behavior.

Do not blindly cherry-pick the nine old infrastructure commits. Compare each file and reapply only compatible intent.

## Implementation order inside one branch

1. **Foundation and identity**
   - branch/workflow rules;
   - Kiyori application IDs and names;
   - launcher/splash resources;
   - AI context;
   - repositories and model scaffolding.

2. **Typed navigation platform**
   - configurable static destinations;
   - mandatory Home;
   - persistence and migrations;
   - bottom bar and rail;
   - settings editor;
   - typed dynamic shortcuts.

3. **Calendar vertical slice**
   - date range and timezone logic;
   - main/nested route split;
   - list/grid;
   - tri-state filter;
   - day/week UI;
   - swipe-between-days improvement.

4. **Home/Discover/Season vertical slice**
   - Home top actions;
   - nested own-profile navigation fix;
   - five Home-list shortcuts;
   - all Anime/Manga chart shortcuts;
   - current/next Season shortcut;
   - picker and removal alignment fix.

5. **Stabilization and release**
   - migration hardening;
   - accessibility;
   - compact/wide/OLED;
   - notification/backup/Wear/resource hardening;
   - full CI;
   - English README/changelog;
   - final APK.

## Internal gate after each slice

Compile affected modules and run affected tests. Do not wait for the owner, but do not carry a known compiler failure into the next slice.

After slices 2–4, run universal FOSS debug assembly.

After slice 5, run the complete matrix from `06_TEST_ONCE_FINAL_MATRIX.md`.

## Reuse over duplication

Reuse current upstream:

- Home current-list full view;
- Discover chart view;
- Season view;
- media list/grid items;
- navigation and Koin patterns;
- AniList queries.

A main-tab host changes route framing and insets; it does not duplicate the data layer.

## Exact old mistakes to avoid

Read `04_KNOWN_FAILURES_AND_PREVENTION.md`.

Especially:

- do not navigate Home account to a hidden Profile main tab;
- do not reference GraphQL fields not selected by the query;
- do not put phone launcher shortcuts in common resources used by Wear;
- do not use disabled Gradle `resValue`;
- do not hardcode release package in debug notification intents;
- do not persist concrete current season/year;
- do not let Home become hidden during normalization;
- do not make dynamic icons indistinguishable;
- do not modify giant upstream files when a wrapper/host parameter works.

## Calendar v2 improvement

The old accepted Calendar had arrows and day taps, but no horizontal day swipe.

Implement:

- swipe left → next date;
- swipe right → previous date;
- crossing Sunday/Monday updates visible week automatically;
- constrain to today through today+14;
- arrows and taps update the same selected-date source;
- counts and content remain synchronized;
- restore selected index after process recreation when valid;
- avoid gesture conflicts;
- keep arrows/day buttons as accessibility alternatives.

Use `blueprints/02_calendar_blueprint.md`.

## Deliverables before owner testing

1. ready-for-review PR;
2. exact upstream and product SHAs;
3. diff summary;
4. full test results;
5. installable universal FOSS debug APK;
6. optional GMS and Wear debug APKs;
7. APK SHA-256;
8. screenshot checklist;
9. concise known limitations;
10. updated AI handoff.

Do not version-bump merely to show progress. `1.6.0.1` is final release work after the rebuilt FOSS debug APK is approved.

---


<!-- END reconstruction/01_AI_BOOTSTRAP_SPEEDRUN_PROMPT.md -->


---

<!-- BEGIN reconstruction/02_SPEEDRUN_MASTER_PLAN.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Integrated Speedrun Master Plan

## Objective

Recreate all accepted Kiyori functionality from a current upstream base in one integrated development branch, while preserving the safety benefits of incremental internal commits and CI.

## Scope included

### Identity

- `app.kiyori`
- `app.kiyori.debug`
- `Kiyori`
- `Kiyori Debug`
- Kiyori launcher assets
- dedicated borderless splash mark
- English public README and changelog
- internal namespace remains `com.axiel7.anihyou`

### Primary navigation

- two to five visible destinations;
- mandatory Home;
- optional Profile;
- reorder and visibility;
- reset;
- one configuration for bottom bar and rail;
- typed static and dynamic entries;
- safe fallback;
- migration from legacy/static forms;
- add-main-tab picker;
- remove dynamic shortcuts;
- corrected X/remove alignment.

### Calendar

- true configurable main tab;
- existing nested Calendar preserved;
- today through today+14 inclusive;
- Monday–Sunday week;
- counts per day;
- selected thin underline;
- list default;
- grid persisted;
- compact tri-state filter;
- list item details and scores;
- timezone/DST;
- swipe left/right between days;
- week changes automatically at boundary.

### Home

- existing `Aktuell` and `Aktivität` tabs preserved;
- Notifications, Settings, Account actions;
- Account opens nested own profile;
- current lists reusable as main tabs.

### Dynamic shortcuts

Home lists:

- Airing;
- Behind;
- Watching;
- Reading;
- Next Season.

Discover:

- Current Season;
- Next Season;
- Top 100 Anime;
- Popular Anime;
- Upcoming Anime;
- Airing Anime;
- Top Movies;
- Top 100 Manga;
- Popular Manga;
- Upcoming Manga;
- Releasing Manga.

### Stabilization

- malformed config repair;
- migration tests;
- TalkBack and keyboard actions;
- compact/broad layouts;
- OLED;
- FOSS/GMS/Wear;
- notification routing;
- notification branding;
- token backup exclusion;
- phone/Wear resource separation;
- R8/lint;
- upstream compatibility.

## Speedrun execution design

### Sprint A — Foundation and navigation core

Build one coherent model before UI:

- `MainNavigationDestinationId`
- `MainNavigationShortcut`
- `MainNavigationItem`
- `MainNavigationConfig`
- codec/versioning
- preferences repository
- ViewModel state
- dynamic destination resolver
- tests

Then wire bottom bar and rail.

### Sprint B — Editor and routes

Build:

- settings editor;
- mandatory Home;
- add sheet;
- category rows;
- dynamic add/remove;
- route wrappers;
- nested/main classification;
- Home account route fix.

### Sprint C — Calendar

Build model/tests first, then UI:

- date range;
- week calculation;
- timezone bounds;
- filter;
- ViewModels;
- main/nested host;
- list/grid;
- swipe pager.

### Sprint D — Reused feature hosts

Wrap existing:

- Home full-list;
- charts;
- Season.

No new queries unless current upstream truly lacks required fields.

### Sprint E — Hardening and release readiness

Apply all Phase-4 contracts and full CI.

## Commit plan

Suggested sequence:

```text
chore: establish Kiyori identity and recovery context
feat: add typed configurable main navigation model
feat: persist and normalize main navigation configuration
feat: drive bottom navigation and rail from shared registry
feat: add main navigation editor and shortcut picker
feat: add date-based calendar main destination
feat: add swipeable calendar day navigation
feat: expose Home lists and Discover charts as main shortcuts
feat: add dynamic current and next season destination
fix: route Home account action to nested own profile
fix: align dynamic shortcut remove controls
test: cover navigation migration and route contracts
chore: harden backup notifications Wear and splash
ci: validate FOSS GMS Wear lint and R8
docs: prepare English Kiyori release documentation
```

Each commit should compile or be immediately followed by a fix before advancing.

## Owner test strategy

The owner performs one consolidated device test after the full integrated build.

The AI still runs automated checks continuously. “Test once” means one owner acceptance cycle, not one build at the very end.

## Success state

The final FOSS debug build meets or exceeds the supplied screenshots, with Calendar day swipe and corrected remove-button alignment.


<!-- END reconstruction/02_SPEEDRUN_MASTER_PLAN.md -->


---

<!-- BEGIN reconstruction/03_RECOVERED_IMPLEMENTATION_MAP.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Recovered Implementation Map

This is the most detailed recoverable map of the lost implementation. Names and paths are historically recorded, but current upstream may require adaptation.

## Core model

Historically introduced:

```text
core/model/src/main/java/com/axiel7/anihyou/core/model/navigation/
  MainNavigationConfig.kt
  MainNavigationShortcut.kt
```

Known concepts:

- `MAIN_NAVIGATION_CONFIG_VERSION`
- final historical config version `3`
- `MIN_VISIBLE_MAIN_DESTINATIONS = 2`
- `MAX_VISIBLE_MAIN_DESTINATIONS = 5`
- static IDs:
  - HOME
  - ANIME
  - MANGA
  - PROFILE
  - EXPLORE
  - CALENDAR
- Calendar stable ID `"calendar"`, legacy index `5`, default hidden
- Home always visible after final normalization
- Profile default hidden in new/reset config
- migration preserves an already-visible Profile
- shortcut IDs encode type and semantic parameter

Typed shortcuts:

```text
Season(mode = CURRENT | NEXT)
CurrentList(type)
Chart(type)
```

Persist mode/type only, never localized text or resolved season/year.

## Preferences

Historically introduced:

```text
core/domain/.../MainNavigationPreferencesRepository.kt
core/domain/.../CalendarPreferencesRepository.kt
```

Known behavior:

- main navigation stored as encoded versioned string;
- Calendar list/grid stored as Boolean, default list;
- Calendar filter reused nullable Boolean:
  - null = all
  - true = only on list
  - false = hide on list

Repositories were registered in a Koin repository module.

## Shared route layer

Historically changed:

```text
core/ui/.../BottomDestination.kt
core/ui/.../navigation/Routes.kt
core/ui/.../navigation/NavActionManager.kt
core/ui/.../navigation/NavigationState.kt
```

Known route split:

- `Routes.CalendarMain`
- existing nested `Routes.Calendar`
- `Routes.SeasonMain`
- existing nested Season route
- main chart route with chart parameter
- main current-list route with list parameter
- nested existing chart/list routes retained

Top-level routes:

- no back arrow;
- receive main bottom/rail inset;
- participate in selected destination logic.

Nested routes:

- retain back arrow;
- preserve original behavior.

## App shell

Historically changed:

```text
app/.../MainActivity.kt
app/.../MainNavigation.kt
app/.../MainViewModel.kt
app/.../composables/MainBottomNavBar.kt
app/.../composables/MainNavigationRail.kt
```

Known behavior:

- normal app start always Home;
- Calendar and dynamic shortcuts never become persisted startup page;
- hidden/removed active destination → Home;
- bottom and rail resolve one shared model;
- season labels refresh on app resume;
- dynamic ViewModel keys include semantic parameters;
- navigation stacks remain saveable.

## Navigation settings

Historically introduced:

```text
feature/settings/.../navigation/
  MainNavigationSettingsEvent.kt
  MainNavigationSettingsUiState.kt
  MainNavigationSettingsView.kt
  MainNavigationSettingsViewModel.kt
```

Editor behavior:

- reset;
- instructions;
- mandatory Home;
- switches;
- drag handles;
- plus FAB;
- add sheet;
- Snackbar for maximum five;
- dynamic entries removable.

New v2 correction:

- place remove/X in fixed 48dp action slot;
- center vertically with switch/drag actions;
- avoid extra bottom padding;
- test large font scale.

## Calendar

Historically introduced/changed:

```text
feature/calendar/.../CalendarDateRange.kt
feature/calendar/.../CalendarListFilter.kt
feature/calendar/.../CalendarHostViewModel.kt
feature/calendar/.../CalendarUiState.kt
feature/calendar/.../CalendarViewModel.kt
feature/calendar/.../CalendarView.kt
```

Known behavior:

- `LocalDate`
- `ZoneId.systemDefault()`
- timezone-aware day start/end
- 15 selectable dates
- Monday week start
- final date today+14
- week arrows stop at boundaries
- seven equal-width day targets
- weekday abbreviation
- large day number
- count
- selected 2dp primary line
- keyed ViewModel per selected date
- counts requested historically with `perPage=50`
- list used existing `MediaItemHorizontal`
- grid used existing `MediaItemVertical`
- list showed title, poster, episode, local time, `SmallScoreIndicator`, list status
- query had `meanScore`
- historical query did not have `genres`

New v2:

- one selected-date state drives taps, arrows, and pager;
- crossing week boundary updates header;
- day swipe clamped to 15-day window.

## Home and profile

Historically changed:

```text
feature/home/.../HomeView.kt
feature/home/.../current/fulllist/CurrentFullListView.kt
```

Top actions:

- notifications;
- settings;
- account.

Critical fix:

First implementation called optional Profile main destination. Because Profile was hidden, safety logic returned Home.

Correct behavior:

- Home account navigates to nested own-profile route;
- optional Profile main tab remains independent.

## Charts and Season

Historically changed:

```text
feature/explore/.../charts/MediaChartListView.kt
feature/explore/.../season/SeasonAnimeView.kt
```

Pattern:

- add host parameters/defaults;
- nested caller gets existing back arrow/padding;
- main caller gets no back arrow and main inset;
- reuse existing ViewModel/query/pagination.

Season:

- current/next mode persisted;
- season/year resolved at runtime;
- bottom/rail label season only;
- page title season + year;
- ViewModel key season + year;
- refresh on resume.

## Phase-4 operational changes

Historically included:

- token DataStore excluded from backup/transfer;
- notification worker used installed package;
- Kiyori notification mark;
- dedicated `kiyori_splash_mark`;
- phone shortcut XML moved to app module;
- debug/release resource overlays for target package;
- Wear compileSdk 37, targetSdk 36;
- Kiyori Wear strings moved to Wear-local resources;
- core translations restored close to upstream;
- migration contract tests;
- FOSS/GMS/Wear/lint/R8 CI.

## Historical test concepts

Tests covered:

- legacy visible list migration preserving Profile;
- v2 Calendar visibility migration;
- final schema round trip;
- malformed over-capacity repair;
- unknown typed parameter discard;
- Home protection;
- shortcut serialization;
- multiple distinct shortcuts;
- exact duplicate removal;
- Season singleton;
- current/next season year boundary;
- route separation;
- Calendar filter states;
- timezone/DST;
- DataStore persistence;
- Home account route.


<!-- END reconstruction/03_RECOVERED_IMPLEMENTATION_MAP.md -->


---

<!-- BEGIN reconstruction/04_KNOWN_FAILURES_AND_PREVENTION.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Known Failures and Prevention

These occurred during the prior implementation.

## 1. GraphQL field assumption

Calendar list code tried to read `genres` from `AiringAnimesQuery.Media`, but the query did not select it.

Fix: remove genre display or explicitly update the GraphQL operation and generated model after reviewing current upstream schema.

Rule: inspect the exact generated operation result type. `meanScore` was historically available; `genres` was not.

## 2. Hidden Profile no-op

Home account navigated to the optional Profile main tab. Profile was hidden, so safety logic immediately returned Home and the button appeared dead.

Fix: navigate to the nested own-profile route.

Test: Home account action must not target the configurable Profile main destination.

## 3. Missing Compose Foundation import

The add-sheet/picker failed to compile due to a missing Foundation import.

Prevention: compile the settings feature immediately after introducing the scrollable sheet and adapt imports to the exact current Compose API.

## 4. Unsupported `matchParentSize`

The project’s Compose version did not expose `matchParentSize()` in the used context.

Fix: use a fixed constrained size or `fillMaxSize()` inside an explicitly sized parent.

Rule: do not assume APIs from a newer Compose version.

## 5. Wear compile SDK mismatch

Wear compiled against API 36 while shared libraries required API 37.

Fix: raise Wear `compileSdk` to 37, retain `targetSdk` 36 unless intentionally changed.

## 6. Phone shortcut leaked into Wear

Phone launcher shortcut XML was in common resources and linked into Wear.

Fix: move it into the phone app module.

## 7. Disabled Gradle `resValue`

Variant-specific package string was attempted with `resValue`, but the project disabled it.

Fix: use Android resource overlays under `app/src/main/res` and `app/src/debug/res`.

## 8. Hardcoded release package in notifications

Notification intent targeted release package in debug.

Fix: resolve installed package with `applicationContext.packageName` or equivalent.

## 9. Accidental whole-file replacement

A small version-catalog/build change accidentally replaced or truncated large files.

Prevention:

- fetch current blob;
- apply minimal patch;
- inspect complete diff;
- verify only intended lines;
- never reconstruct large Gradle/version files from memory.

## 10. Dynamic icon ambiguity

Some Home-list shortcuts reused icons indistinguishable from static Anime/Manga/Season destinations.

Fix: base icon plus small decorative list/check or Anime/Manga marker.

Accessibility: decorative overlay has no separate content description.

## 11. Current season stale across rollover

Persisting concrete season/year or keying only by mode can become stale.

Fix: persist only CURRENT/NEXT; resolve season/year at runtime; include season/year in ViewModel key; refresh label on resume.

## 12. Calendar counts and list diverge

Header counts and content can use different filters or date boundaries.

Fix: host owns one filter state and one date-bound function shared by counts and content.

## 13. Home hidden through migration

Malformed/old config may encode Home hidden or over-capacity before Home.

Fix: ensure Home exists and is visible before enforcing max; never hide Home.

## 14. Remove/X alignment drift

Observed defect: dynamic shortcut remove button sits lower than adjacent controls.

Fix:

- shared row minimum height;
- `verticalAlignment = Alignment.CenterVertically`;
- fixed 48dp action slots;
- no extra bottom padding;
- separate text and action columns;
- test font scales 1.0, 1.3, 1.5.

## 15. Swipe pager state bugs

New v2 risks:

- pager and date desynchronize;
- week header lags;
- rapid swipe crosses range;
- process recreation restores invalid date;
- gesture conflicts.

Rules:

- page index is canonical;
- derive date as today + page;
- clamp 0..14;
- derive week from date;
- observe settled page;
- arrows/day taps animate pager to index;
- restore bounded index;
- prefetch only adjacent date;
- retain non-swipe controls.


<!-- END reconstruction/04_KNOWN_FAILURES_AND_PREVENTION.md -->


---

<!-- BEGIN reconstruction/05_UI_UX_CONTRACT_AND_IMPROVEMENTS.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# UI/UX Contract and v2 Improvements

## Source of truth

The five supplied screenshots are the accepted visual baseline. The v2 rebuild may improve stability and interaction, but must not unintentionally redesign typography, spacing, color system, or Material behavior.

## Main navigation editor

Must show:

- `Hauptnavigation anpassen`;
- reset action;
- explanatory text;
- highlighted two-to-five rule;
- Start mandatory;
- rows for static destinations;
- switches;
- drag handles;
- plus FAB.

Dynamic row improvement:

- remove X centered with switch/drag controls;
- touch target at least 48dp;
- no downward offset;
- TalkBack label names the shortcut.

## Add-main-tab sheet

Categories:

- Start;
- Discover/Entdecken.

Must contain all accepted options, not only Season.

Rows:

- icon;
- title;
- concise description;
- full-width touch target;
- scrollable;
- no giant outlined cards.

At capacity:

- add hidden;
- Snackbar explains maximum five.

## Home

Preserve:

- title;
- `Aktuell` and `Aktivität`;
- notification badge;
- settings;
- account;
- existing horizontal sections;
- +1 controls;
- bottom inset.

Account must navigate.

## Calendar accepted design

Header:

- title `Kalender`;
- grid/list toggle;
- compact filter;
- centered week range;
- left/right arrows;
- seven day columns;
- count per day;
- thin underline selection.

List:

- poster;
- title;
- episode;
- local airing time;
- score;
- list status;
- existing component styling.

## Calendar v2 day swipe

Desired behavior:

- swipe left → next day;
- swipe right → previous day;
- Sunday→Monday advances week;
- Monday→Sunday reverse returns week;
- cannot leave today..today+14;
- arrows and date taps still work;
- screen readers and keyboard users have alternatives.

Animation:

- restrained Material pager;
- do not animate top bar independently;
- underline and content settle together.

Loading/performance:

- retain header immediately;
- avoid blank flashes;
- cache adjacent day when practical;
- prefetch only immediate neighbor, not all 15 heavy pages.

## OLED, Light, Dark

Avoid:

- hardcoded black except deliberate OLED surface;
- white borders;
- card outlines around every row;
- fixed purple values outside theme tokens.

Use Material theme color roles.

## Splash

Use dedicated transparent mark. System splash must not show the white rim from the launcher image.

## Wide display

Navigation rail uses same items/order. Content receives correct insets. Editor/sheet uses sensible max width.

## Text/localization

Public repository documentation is English.

Application UI uses existing localization. Add English source strings and German translations at minimum for accepted German screens. Do not manually edit every upstream translation.


<!-- END reconstruction/05_UI_UX_CONTRACT_AND_IMPROVEMENTS.md -->


---

<!-- BEGIN reconstruction/06_TEST_ONCE_FINAL_MATRIX.md -->

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


<!-- END reconstruction/06_TEST_ONCE_FINAL_MATRIX.md -->


---

<!-- BEGIN reconstruction/07_BRANCH_UPSTREAM_AND_COMMIT_STRATEGY.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Branch, Upstream, and Commit Strategy

## New repository layout

Recommended exact names:

```text
develop
recovery/phase0-backup
main
feature/kiyori-integrated-rebuild
```

## Import rules

### `recovery/phase0-backup`

Push the backup exactly and preserve its Git history. Decide separately whether to commit the staged SDK-channel fix. Never rewrite this branch.

### `develop`

Fetch current:

```text
axiel7/AniHyou-android:develop
```

Verify SHA and make fork `develop` an exact mirror.

### `main`

Create from current `develop`, not the stale Phase-0 backup. Apply Kiyori identity and product work through pull requests.

## Porting Phase-0 infrastructure

Compare:

- workflows;
- Crowdin;
- `.gitignore`;
- `docs/BUILDING.md`;
- `shell.nix`.

Current upstream may have newer actions, SDK requirements, or Gradle tasks. Reapply intent rather than blindly cherry-picking whole files.

## Integrated branch

Create:

```text
feature/kiyori-integrated-rebuild
```

All feature work may happen here, but commits must remain coherent and CI must stay recoverable.

## PR strategy

One large product PR is acceptable for the speedrun only if:

- commits are reviewable;
- no unrelated upstream changes are mixed in;
- full CI is green;
- PR body contains requirement checklist;
- APK artifact is attached;
- owner approves final device test.

## Upstream movement during rebuild

Freeze upstream base SHA in PR description. Do not merge new upstream halfway through unless required for a blocker. After acceptance, perform a separate upstream sync.

## Secrets

Never commit:

- keystore;
- passwords;
- Base64 keystore;
- OAuth tokens;
- local properties;
- APK signing secrets;
- private device logs.

## AI handoff files

Create:

```text
AGENTS.md
docs/ai-context/README.md
docs/ai-context/CURRENT_STATE.md
docs/ai-context/PRODUCT_PLAN.md
docs/ai-context/DECISIONS.md
docs/ai-context/ARCHITECTURE_AND_SCOPES.md
docs/ai-context/UPSTREAM_BUILD_AUTH.md
docs/ai-context/PHASE_PROMPTS.md
```

Adapt this package. Do not copy historical “merged” status before the new rebuild reaches it.


<!-- END reconstruction/07_BRANCH_UPSTREAM_AND_COMMIT_STRATEGY.md -->


---

<!-- BEGIN reconstruction/08_APK_FORENSICS_GUIDE.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# APK Forensics Guide

## Is the latest APK useful?

Yes, as supplementary evidence.

It can confirm:

- application ID;
- version name/code;
- resources and strings;
- manifest routes and activities;
- launcher/splash resources;
- packaged class names;
- route constants when not obfuscated;
- preference keys;
- Compose labels;
- Phase-4 assets;
- certificate fingerprint;
- differences between historical APKs.

It cannot reliably recover:

- original Kotlin formatting;
- comments;
- exact variable names after optimization;
- commit history;
- clean Compose structure;
- tests;
- code removed by R8;
- signing private key.

## Best evidence set

When available, provide:

1. latest accepted FOSS debug APK;
2. Phase-2 APK;
3. corrected Phase-3 APK;
4. Phase-4 FOSS debug APK;
5. signed or unsigned release candidate.

Record filenames and SHA-256.

## NixOS tool shell

```bash
nix-shell -p jadx apktool android-tools apksigner unzip zip file ripgrep
```

Package availability can vary by Nix channel.

## Metadata

```bash
apkanalyzer manifest application-id app.apk
apkanalyzer manifest version-name app.apk
apkanalyzer manifest version-code app.apk
apksigner verify --print-certs app.apk
```

## Resources

```bash
apktool d -f app.apk -o decoded
rg -n "Haupttab hinzufügen|Kalender|Nächste Saison|app.kiyori" decoded
```

## Decompiled code

```bash
jadx -d jadx-out app.apk
rg -n "CalendarMain|SeasonMain|MainNavigationShortcut|calendar_list_view" jadx-out
```

## Compare APKs

For each APK record:

- SHA-256;
- version;
- package;
- certificate;
- manifest diff;
- resources diff;
- class-name diff;
- asset diff.

## Use of findings

Use APK evidence to refine and confirm behavior. Do not paste decompiled code blindly. Reimplement cleanly against current upstream and the GPL source base.

## Highest-value next input

The latest accepted FOSS debug APK is the most useful single additional artifact.


<!-- END reconstruction/08_APK_FORENSICS_GUIDE.md -->


---

<!-- BEGIN reconstruction/09_RELEASE_RECONSTRUCTION.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Release Reconstruction

Release work is last.

## Intended release

```text
name = 1.6.0.1
code = 113
tag = v1.6.0.1
title = Kiyori 1.6.0.1
asset = Kiyori-1.6.0.1-foss-universal.apk
```

## Do not rush version bump

The product rebuild matters more than version text. Keep development versioning unchanged until final FOSS debug is approved.

## Signing identity

Before generating a new permanent key, inspect saved APKs:

```bash
apksigner verify --print-certs APK_FILE
```

If a signed `app.kiyori` production APK was distributed, preserve its signing identity.

If only debug APKs and unsigned candidates exist, create one new permanent release key and back it up.

## Public documentation

English:

- README;
- changelog;
- GitHub release title/notes.

Application UI may remain localized.

## Final workflow

- main only;
- full CI first;
- signing secrets;
- build FOSS release;
- `apksigner verify`;
- upload universal APK;
- create tag;
- create GitHub Release;
- record APK/certificate SHA-256;
- remove temporary keystore.


<!-- END reconstruction/09_RELEASE_RECONSTRUCTION.md -->


---

<!-- BEGIN reconstruction/10_OWNER_FEEDBACK_LEDGER.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Owner Feedback Ledger

This ledger captures the small corrections and preferences that were easy to lose.

## Navigation design

- The Discover-style button design was liked.
- Buttons should be larger and arranged differently, not fundamentally restyled.
- Anime and Manga should receive the richer Discover-style navigation treatment.
- Maximum visible main destinations remains five.
- Calendar should be available as a main tab and work in bottom bar and rail.
- Home/Start is mandatory in the final model.
- Profile should not be visible by default after reset/new install, but existing visible Profile must survive migration.
- Dynamic shortcuts must be removable, reorderable, hideable, and re-enableable.

## Add-main-tab picker

Initial implementation was incomplete because it exposed mainly Season options.

Required additions:

### Start

- Airing / Läuft gerade
- Behind / Ausstehende Folgen
- Watching / Am Schauen
- Reading / Am Lesen
- Next Season / Nächste Saison

### Discover

- Current Season
- Next Season
- Top 100 Anime
- Popular Anime
- Upcoming Anime
- Airing Anime
- Top Movies
- Top 100 Manga
- Popular Manga
- Upcoming Manga
- Releasing Manga

All entries should reuse existing pages.

## Home top bar

Accepted placement, focus, and size:

- notifications;
- settings;
- account.

Bug found:

- account icon was visible and looked correct but did nothing.

Root cause:

- it navigated to hidden Profile main tab and immediately fell back to Home.

Final rule:

- account opens nested own-profile page.

## Calendar

Original accepted direction:

- date-based Calendar;
- today selected;
- Monday–Sunday;
- today through today+14;
- week arrows;
- thin underline;
- list default;
- grid available/persisted.

Important later correction:

- replace the large “On my list” control with a compact filter button next to list/grid.
- filter states:
  - all;
  - only on list;
  - exclude on list.

New rebuild improvement requested now:

- swipe horizontally between days;
- swipe through Sunday into next week;
- reverse swipe into prior week;
- keep arrow and tap controls.

## Navigation editor visual defect

Newly reported:

- remove/X button for added dynamic shortcuts sits slightly lower than neighboring controls.

v2 fix:

- common 48dp action slots;
- center all row actions vertically;
- no extra bottom padding;
- test large font scale.

## Splash

Reported defect:

- white/light border around Kiyori logo in loading screen.

Accepted correction:

- dedicated transparent splash mark;
- only the Kiyori mark;
- larger;
- no white rim;
- launcher icon remains separate.

## Testing cadence

Old process used phase-by-phase owner testing.

New preference:

- speedrun integrated implementation;
- one final consolidated owner device test;
- automated internal tests and builds still required throughout.

## Release priority

The version string `1.6.0.1` is secondary to determining/rebuilding the correct product state.

Do not spend time on release metadata before the full accepted runtime exists.

## Psychological/project framing

Treat the lost repository as an earlier prototype/test run. Use the recovered knowledge to build a cleaner, more stable second implementation rather than attempting an emotionally costly blind recreation.


<!-- END reconstruction/10_OWNER_FEEDBACK_LEDGER.md -->


---

<!-- BEGIN reconstruction/11_INTEGRATED_REBUILD_COPY_PROMPT.md -->

> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# One Copy-Paste Prompt for the Integrated Rebuild

Use this after the new repository has:

- `develop` = newest verified upstream mirror;
- `recovery/phase0-backup` = imported backup;
- `main` = product base;
- this package committed or attached.

---

Work in the Kiyori repository as the primary implementation engineer.

Read all files in `Kiyori-Speedrun-Recovery`, including `blueprints/`, `SOURCE_BACKUP_AUDIT.md`, and the five screenshots.

Verify first:

1. exact `develop` SHA;
2. exact upstream `axiel7/AniHyou-android:develop` SHA;
3. exact Phase-0 backup SHA;
4. `main` base;
5. auth/API/OAuth/version/build differences between current upstream and backup.

Create `feature/kiyori-integrated-rebuild` from current `main`.

Implement the entire accepted Kiyori product in one integrated campaign, using small buildable commits and automated internal gates. Do not require owner testing between historical phases.

Required outcome:

- Kiyori release/debug IDs and names;
- configurable typed main navigation;
- Home mandatory and startup/fallback;
- optional Profile hidden by default with migration preservation;
- bottom bar and navigation rail parity;
- navigation editor with reset, switches, drag handles, plus sheet, add/remove;
- correctly centered remove/X button;
- date-based Calendar main tab plus unchanged nested Calendar route;
- today through today+14, Monday–Sunday, day counts, week arrows, thin underline;
- list default, grid persisted;
- compact tri-state Calendar filter;
- horizontal swipe between dates with automatic week transition;
- Home notifications/settings/account, with account opening nested own profile;
- five Start-list shortcuts;
- all accepted Anime/Manga/Season Discover shortcuts;
- one Season shortcut only;
- current/next Season resolved at runtime and refreshed on resume;
- reused existing Home list/chart/Season views and ViewModels;
- migration, route, timezone, DST, filter, shortcut, and pager tests;
- token backup exclusion;
- variant-safe notification routing;
- Kiyori notification branding;
- borderless enlarged splash mark;
- phone/Wear resource separation;
- Wear compile/target compatibility;
- FOSS/GMS/Wear/lint/R8 CI;
- English README and changelog;
- AI handoff.

Prevent every issue listed in `04_KNOWN_FAILURES_AND_PREVENTION.md`.

Do not blindly copy reconstructed snippets. Adapt them to current upstream APIs and compile after each vertical slice.

Before owner testing, deliver:

- ready PR;
- full CI evidence;
- installable universal FOSS debug APK;
- APK SHA-256;
- visual checklist against screenshots;
- known limitations;
- exact upstream base;
- updated context.

Do not finalize `1.6.0.1` until the owner approves the rebuilt FOSS debug APK.
---


<!-- END reconstruction/11_INTEGRATED_REBUILD_COPY_PROMPT.md -->


---

<!-- BEGIN blueprints/01_navigation_blueprint.md -->

> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Reconstructed Navigation Blueprint

> Compile-oriented design sketch, not exact deleted source.

## Model

```kotlin
enum class MainNavigationDestinationId(
    val stableId: String,
    val defaultVisible: Boolean,
) {
    HOME("home", true),
    ANIME("anime", true),
    MANGA("manga", true),
    PROFILE("profile", false),
    EXPLORE("explore", true),
    CALENDAR("calendar", false),
}

enum class SeasonShortcutMode { CURRENT, NEXT }

sealed interface MainNavigationShortcut {
    val stableId: String

    data class Season(val mode: SeasonShortcutMode) : MainNavigationShortcut {
        override val stableId = "shortcut_season_${mode.name.lowercase()}"
    }

    data class CurrentList(val type: CurrentListType) : MainNavigationShortcut {
        override val stableId = "shortcut_current_list_${type.name.lowercase()}"
    }

    data class Chart(val type: ChartType) : MainNavigationShortcut {
        override val stableId = "shortcut_chart_${type.name.lowercase()}"
    }
}

data class MainNavigationItem(
    val stableId: String,
    val visible: Boolean,
    val staticDestination: MainNavigationDestinationId? = null,
    val shortcut: MainNavigationShortcut? = null,
) {
    init {
        require((staticDestination == null) xor (shortcut == null))
    }
}
```

## Config normalization

```kotlin
const val MAIN_NAVIGATION_CONFIG_VERSION = 3
const val MIN_VISIBLE_MAIN_DESTINATIONS = 2
const val MAX_VISIBLE_MAIN_DESTINATIONS = 5

data class MainNavigationConfig(
    val version: Int,
    val items: List<MainNavigationItem>,
) {
    fun normalized(): MainNavigationConfig {
        val deduplicated = items
            .filter { it.staticDestination != null || it.shortcut != null }
            .distinctBy(MainNavigationItem::stableId)
            .toMutableList()

        ensureAllStaticDestinationsExist(deduplicated)
        forceHomeVisible(deduplicated)
        enforceSingleSeasonShortcut(deduplicated)
        enforceMaximumVisibleWhileProtectingHome(deduplicated)
        enforceMinimumVisible(deduplicated)

        return copy(
            version = MAIN_NAVIGATION_CONFIG_VERSION,
            items = deduplicated,
        )
    }
}
```

Preserve user order where possible. Never hide Home when reducing over-capacity.

## Default

```kotlin
fun defaultMainNavigationConfig(): MainNavigationConfig =
    MainNavigationConfig(
        version = MAIN_NAVIGATION_CONFIG_VERSION,
        items = listOf(
            static(HOME, visible = true),
            static(ANIME, visible = true),
            static(MANGA, visible = true),
            static(PROFILE, visible = false),
            static(EXPLORE, visible = true),
            static(CALENDAR, visible = false),
        )
    ).normalized()
```

## Codec

Support:

- unversioned legacy comma-separated visible static IDs;
- `v2;id:1,id:0`;
- `v3;stableId:1,...`.

```kotlin
fun decode(raw: String?): MainNavigationConfig = when {
    raw.isNullOrBlank() -> defaultMainNavigationConfig()
    raw.startsWith("v3;") -> decodeV3(raw.removePrefix("v3;"))
    raw.startsWith("v2;") -> migrateV2(raw.removePrefix("v2;"))
    else -> migrateLegacy(raw)
}.normalized()
```

Unknown shortcut parameters are discarded, not replaced randomly.

## Add shortcut

```kotlin
fun MainNavigationConfig.addShortcut(
    shortcut: MainNavigationShortcut,
): MainNavigationConfig {
    if (containsShortcut(shortcut)) return this
    if (shortcut is MainNavigationShortcut.Season && seasonShortcut != null) return this

    val initiallyVisible =
        visibleItems.size < MAX_VISIBLE_MAIN_DESTINATIONS

    return copy(
        items = items + MainNavigationItem(
            stableId = shortcut.stableId,
            visible = initiallyVisible,
            shortcut = shortcut,
        )
    ).normalized()
}
```

ViewModel emits Snackbar when added hidden because capacity is reached.

## Dynamic UI resolver

Create one resolver mapping item to:

- route;
- localized label;
- icon;
- selected predicate.

Bottom bar and rail both consume it. Do not duplicate switch logic in both components.


<!-- END blueprints/01_navigation_blueprint.md -->


---

<!-- BEGIN blueprints/02_calendar_blueprint.md -->

> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Reconstructed Calendar Blueprint with Swipe v2

> Adapt API names to current Compose/upstream versions.

## Date range

```kotlin
data class CalendarDateRange(
    val today: LocalDate,
) {
    val firstDate = today
    val lastDate = today.plusDays(14)
    val pageCount = 15

    fun dateForPage(page: Int): LocalDate =
        firstDate.plusDays(page.coerceIn(0, pageCount - 1).toLong())

    fun pageForDate(date: LocalDate): Int =
        ChronoUnit.DAYS.between(firstDate, date)
            .toInt()
            .coerceIn(0, pageCount - 1)

    fun weekStart(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    fun visibleWeek(date: LocalDate): List<LocalDate> =
        (0L..6L).map { weekStart(date).plusDays(it) }
}
```

## Time bounds

```kotlin
fun LocalDate.toInstantBounds(zoneId: ZoneId): ClosedRange<Instant> {
    val start = atStartOfDay(zoneId).toInstant()
    val endExclusive = plusDays(1).atStartOfDay(zoneId).toInstant()
    return start..endExclusive.minusNanos(1)
}
```

Prefer half-open ranges internally where possible:

```text
[startInclusive, nextDayStartExclusive)
```

## Filter

```kotlin
enum class CalendarListFilter {
    ALL,
    ONLY_ON_LIST,
    HIDE_ON_LIST;

    fun toPreference(): Boolean? = when (this) {
        ALL -> null
        ONLY_ON_LIST -> true
        HIDE_ON_LIST -> false
    }

    companion object {
        fun fromPreference(value: Boolean?): CalendarListFilter = when (value) {
            null -> ALL
            true -> ONLY_ON_LIST
            false -> HIDE_ON_LIST
        }
    }
}
```

## Host state

```kotlin
data class CalendarHostUiState(
    val range: CalendarDateRange,
    val selectedPage: Int = 0,
    val listMode: Boolean = true,
    val filter: CalendarListFilter = CalendarListFilter.ALL,
) {
    val selectedDate get() = range.dateForPage(selectedPage)
    val visibleWeek get() = range.visibleWeek(selectedDate)
}
```

## Swipe pager

```kotlin
@Composable
fun SwipeableCalendarContent(
    state: CalendarHostUiState,
    onPageSelected: (Int) -> Unit,
    pageContent: @Composable (LocalDate) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedPage,
        pageCount = { state.range.pageCount },
    )

    LaunchedEffect(state.selectedPage) {
        if (pagerState.currentPage != state.selectedPage) {
            pagerState.animateScrollToPage(state.selectedPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect(onPageSelected)
    }

    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
    ) { page ->
        pageContent(state.range.dateForPage(page))
    }
}
```

If current Compose uses another pager API, adapt without upgrading Compose solely for this.

## Header actions

All actions change selected page.

```kotlin
fun selectDate(date: LocalDate) =
    updatePage(range.pageForDate(date))

fun previousWeek() =
    updatePage((selectedPage - 7).coerceAtLeast(0))

fun nextWeek() =
    updatePage((selectedPage + 7).coerceAtMost(range.pageCount - 1))
```

Historical arrows are week arrows. Swipe changes one day.

## ViewModel identity

Selected-day content ViewModel key:

```kotlin
val key = "calendar-$selectedDate"
```

Counts/content receive same filter.

## Performance

- one page visible;
- one adjacent page prefetched;
- avoid 15 permanent heavy ViewModels;
- cache repository results;
- cancel stale requests;
- stable keys.

## Tests

- page 0 = today;
- page 14 = today+14;
- swipe 6→7 changes week;
- reverse 7→6 returns week;
- arrows clamp;
- saved page clamps;
- DST bounds;
- filter consistency.


<!-- END blueprints/02_calendar_blueprint.md -->


---

<!-- BEGIN blueprints/03_routes_hosts_profile_blueprint.md -->

> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Routes, Reused Hosts, and Profile Blueprint

## Routes

Use current upstream typed/serializable route conventions.

Conceptually:

```kotlin
sealed interface Routes {
    data object Calendar : Routes
    data object CalendarMain : Routes

    data class SeasonAnime(
        val season: MediaSeason,
        val year: Int,
    ) : Routes

    data class SeasonMain(
        val mode: SeasonShortcutMode,
    ) : Routes

    data class ChartMain(
        val chartType: ChartType,
    ) : Routes

    data class CurrentListMain(
        val listType: CurrentListType,
    ) : Routes
}
```

## Host parameter pattern

Do not clone views.

```kotlin
@Composable
fun SeasonAnimeView(
    season: MediaSeason,
    year: Int,
    isMainDestination: Boolean = false,
    onBack: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
) {
    ExistingSeasonScaffold(
        showBack = !isMainDestination,
        contentPadding = contentPadding,
    )
}
```

Original nested call uses defaults. Main call sets `isMainDestination = true`.

Use same approach for charts and current full lists.

## Home account

Bad:

```kotlin
onAccountClick = { navigateToMainDestination(PROFILE) }
```

Correct concept:

```kotlin
onAccountClick = navActionManager::toOwnProfile
```

Nested route is not filtered by visible main destinations.

## Top-level selection

Map current route to exact `MainNavigationItem.stableId`.

Parameterized routes include parameter identity so two chart shortcuts coexist.

## Hidden active destination

```kotlin
if (activeMainItemStableId !in visibleStableIds) {
    navigateToHome(clearTopLevelSelection = true)
}
```

Do not apply to nested profile/details routes.

## Startup

Always establish Home as initial top-level destination. Do not restore Calendar/Season/chart as automatic startup.


<!-- END blueprints/03_routes_hosts_profile_blueprint.md -->


---

<!-- BEGIN blueprints/04_settings_editor_alignment_blueprint.md -->

> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Navigation Editor and Remove Alignment Blueprint

## Row layout

```kotlin
@Composable
fun NavigationEditorRow(
    title: String,
    subtitle: String?,
    icon: ImageVector,
    visible: Boolean,
    mandatory: Boolean,
    removable: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
    dragHandle: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Switch(
                checked = visible,
                onCheckedChange = if (mandatory) null else onVisibilityChange,
            )
        }

        if (removable) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(
                        R.string.remove_main_navigation_shortcut,
                        title,
                    ),
                )
            }
        } else {
            Box(Modifier.size(48.dp))
        }

        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            dragHandle()
        }
    }
}
```

## Why this fixes the old defect

- every action occupies the same 48dp slot;
- all slots share center alignment;
- text height cannot shift actions;
- no custom bottom padding on remove;
- static/dynamic rows preserve column geometry.

## Accessibility

Drag handle should expose:

- move up;
- move down;
- current position.

Remove button names the shortcut.

## Picker

Use a lazy list in a Material bottom sheet. Do not place all options in an unbounded Column.


<!-- END blueprints/04_settings_editor_alignment_blueprint.md -->


---

<!-- BEGIN blueprints/05_ci_security_release_blueprint.md -->

> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# CI, Security, Wear, and Release Blueprint

## Verification matrix

Workflow builds/tests:

- FOSS debug;
- GMS debug;
- FOSS tests;
- GMS tests;
- model tests;
- domain tests;
- Calendar tests;
- Wear debug;
- FOSS lint;
- GMS lint;
- minified FOSS release candidate.

Upload separate artifact groups.

## Backup

Confirm actual DataStore path, then exclude credential store from:

- `backup_rules.xml`;
- `data_extraction_rules.xml`.

Add a CI contract check.

## Notifications

Resolve installed package at runtime. Use Kiyori icon and installed variant label.

## Phone shortcuts

Keep shortcut XML in phone app resources. Use main/debug resource overlays for target package when necessary.

## Wear

- compileSdk 37 when required;
- keep targetSdk 36 unless intentionally changed;
- Kiyori-specific phone-required text in Wear module;
- do not edit every shared translation.

## Splash

Theme points to dedicated transparent `kiyori_splash_mark`, not the rimmed launcher bitmap.

## Release

On main/manual gate:

- verify four secrets;
- decode temporary keystore;
- build signed FOSS release;
- verify with `apksigner`;
- stage universal APK;
- create English notes from changelog;
- upload artifact;
- create tag/release;
- remove temporary keystore.


<!-- END blueprints/05_ci_security_release_blueprint.md -->
