# Media row alignment regression fix — 2026-08-23

## Symptom

After the 2026-08-23 upstream integration, horizontal media rows in Kiyori were visibly shifted compared with the pre-sync / reference geometry. The issue was reproduced visually in Top 100 and Calendar list mode.

On the supplied 912×2048 screenshots:

- poster left edge and poster size remained effectively unchanged;
- the first Top 100 text column began about 8–9 px farther to the right;
- the first Top 100 title began about 34 px lower;
- Calendar showed the same horizontal media-row geometry because it uses the same shared `MediaItemHorizontal` composable.

## Root cause

The upstream fast-scroll stability fix replaced Material3 `ListItem` with a custom `Surface` + `Row` + `combinedClickable` implementation. That replacement correctly avoided the `ListItem`/lazy-list crash path, but it did not preserve the previous layout geometry:

- poster-to-text spacing became `16.dp`;
- the whole row used `Alignment.CenterVertically`.

The reference layout behaves like the previous tall `ListItem`: the text column starts roughly 12.dp after the poster and is top-aligned for a tall poster row.

## Fix

`core/ui/.../media/MediaItemHorizontal.kt` keeps the crash-safe custom implementation and restores the reference geometry:

- poster-to-text spacing: `12.dp`;
- row child alignment: `Alignment.Top`;
- placeholder text spacing uses the same 12.dp value.

Do **not** revert to Material3 `ListItem`; doing so would risk reintroducing the fast-scroll crash that motivated the upstream replacement.

## Expected affected screens

Any screen using `MediaItemHorizontal`, including at least:

- Top 100 / media charts;
- Calendar list mode;
- other horizontal media lists that use the same shared composable.

## Manual verification

Compare with the supplied reference screenshot on the same device/display scaling:

1. Top 100: poster left edge should remain unchanged; title/subtitles should move left by roughly 4.dp and upward to the top-aligned reference position.
2. Calendar list: poster left edge should remain unchanged; title/airing-time/score should use the same restored poster-to-text gap and top alignment.
3. Fast-fling long media lists repeatedly to verify there is no crash/ANR regression.
4. Long-press a media row and verify the edit interaction still works.
