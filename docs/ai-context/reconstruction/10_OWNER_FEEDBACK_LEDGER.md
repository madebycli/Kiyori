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
