# Kiyori Product Plan — Integrated Speedrun Rebuild

## Goal

Rebuild the previously accepted product experience on the newer upstream base in one integrated implementation campaign, using internal automated gates and one consolidated owner test.

## Slice 1 — Kiyori identity and foundation

- release application ID `app.kiyori`;
- debug application ID `app.kiyori.debug`;
- launcher names `Kiyori` and `Kiyori Debug`;
- Kiyori launcher and dedicated transparent splash assets;
- preserve AniList OAuth/API behavior and internal namespace;
- adapt current upstream CI/NixOS rather than copying stale full files.

### Progress

- Completed: release/debug identity plus phone-scoped launcher, adaptive, monochrome, legacy, splash,
  SVG source, and 512×512 preview assets.
- Remaining: repository-facing English branding and a Gradle validation when the pinned wrapper distribution is available.

## Slice 2 — Typed configurable main navigation

- two to five visible targets;
- Home mandatory, startup and safe fallback;
- Profile hidden in new/reset defaults but preserved by migration when already visible;
- shared configuration for bottom navigation and navigation rail;
- stable static IDs and typed dynamic shortcuts;
- reorder, visibility, reset, add and remove;
- fixed 48dp centered remove/X action slot;
- robust legacy/v2/final-schema normalization.

### Progress

- Completed locally: versioned typed item/shortcut model and codec; legacy/v2 migration;
  deterministic repair for unknown IDs, duplicates, Home visibility, capacity, minimum visibility,
  and duplicate Season shortcuts.
- Next: persist the normalized schema through the existing preferences DataStore.

## Slice 3 — Date-based Calendar

- configurable true main destination plus unchanged nested route;
- today through today+14 inclusive;
- Monday–Sunday week;
- day counts and thin accent selection line;
- list default, grid retained and persisted;
- compact tri-state filter: all / only on list / hide on list;
- timezone and DST-safe bounds;
- swipe left/right between days;
- week header changes automatically across Sunday/Monday;
- arrows and day taps remain accessible alternatives.

## Slice 4 — Home, Discover and Season shortcuts

Home top actions:

1. Notifications
2. Settings
3. Account

Account opens the nested own-profile route and must not depend on optional Profile main-tab visibility.

Home shortcuts:

- Airing;
- Behind;
- Watching;
- Reading;
- Next Season.

Discover shortcuts:

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

Reuse current upstream full-list, chart and Season views/ViewModels. Main hosts remove the back arrow; original nested entry points remain unchanged.

## Slice 5 — Stabilization

- migration and malformed-config tests;
- TalkBack, keyboard/D-pad, 48dp touch targets;
- compact, wide/rail, Light, Dark and OLED;
- notification package resolved from installed variant;
- auth-token DataStore excluded from backup and transfer;
- phone launcher shortcuts isolated from Wear;
- Kiyori-specific Wear text kept in Wear overrides;
- Wear compile SDK compatible with current dependencies while target changes remain explicit;
- FOSS, GMS, Wear, tests, lint and minified release candidate in CI.

## Test strategy

No repeated owner approval after every historical phase. The coding AI must keep internal slices compiling and tested. After all slices, produce one installable universal FOSS debug APK for consolidated owner acceptance.

## Release

The previous planned number `1.6.0.1` is historical and not important during reconstruction. Choose final Kiyori release metadata only after runtime acceptance, keeping README, changelog and release notes in English.
