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
