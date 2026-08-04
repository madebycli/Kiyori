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
