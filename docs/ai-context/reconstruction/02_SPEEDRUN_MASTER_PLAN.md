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
