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
