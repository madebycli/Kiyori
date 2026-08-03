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
