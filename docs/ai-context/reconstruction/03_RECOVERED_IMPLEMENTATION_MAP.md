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
