# Historical Recovery Context

> Historical evidence only. The active product name is Kiyori, and current files outside this directory take precedence.

# Navori Recovery — All-in-One Context

> Reconstructed from the surviving project conversation and owner-provided screenshots. This is not a byte-for-byte source backup. A new AI must audit the surviving local tree before changing code.


---

<!-- BEGIN 00_README_RECOVERY_PACKAGE.md -->

# Navori Recovery Package

## Purpose

This package reconstructs the Navori product plan, implementation history, architecture boundaries, phase prompts, release knowledge, and visual acceptance targets from the surviving ChatGPT conversation and the screenshots supplied by the owner.

The original GitHub repository is no longer available. Therefore this package is **not a byte-for-byte copy of the lost repository**. It is a structured recovery record intended to let a new AI inspect a surviving local source tree, determine exactly which milestones are present, upload that tree safely to a new repository, and continue only the missing work.

## Evidence levels

Every future AI must distinguish these categories:

- **Conversation-verified**: explicitly recorded in the surviving chat, including branch names, commit SHAs, workflow results, feature decisions, hashes, and manual approvals.
- **Screenshot-verified**: directly visible in the five supplied screenshots.
- **Expected implementation**: source paths, classes, routes, tests, and behavior that were recorded as implemented, but must be confirmed in the surviving local tree.
- **Unknown until forensic audit**: whether the local tree contains Phase 1, 2, 3, Phase 4, the `1.6.0.1` version bump, release automation, Git history, signing configuration, or only a subset.

Do not silently promote an expected fact to a verified local fact.

## Recommended reading order

1. `02_NEW_AI_BOOTSTRAP_PROMPT.md`
2. `03_REPOSITORY_FORENSIC_AUDIT_PLAN.md`
3. `01_RECOVERED_CURRENT_STATE.md`
4. `04_PRODUCT_MASTER_PLAN.md`
5. `06_PRODUCT_REQUIREMENTS_AND_DECISIONS.md`
6. `07_ARCHITECTURE_BUILD_AUTH_GUARDRAILS.md`
7. `10_ACCEPTANCE_AND_TEST_MATRIX.md`
8. `09_RELEASE_1_6_0_1_RECONSTRUCTION.md`
9. `08_VISUAL_ACCEPTANCE_REFERENCE.md`

Use `NAVORI_RECOVERY_ALL_IN_ONE.md` when a single context file is preferable.

## Recovery rule

The first task is **not** to reimplement everything. The first task is to prove what survives.

The new AI must:

1. make immutable backups of the local directory and any `.git` directory;
2. inspect Git refs, reflogs, bundles, patches, version files, source markers, tests, and build outputs;
3. create a phase-by-phase evidence matrix;
4. upload the preserved state to a new private or public repository only after the owner supplies the destination;
5. continue from the first genuinely missing acceptance criterion.

## Supplied visual references

The package contains:

- `screenshots/latest-add-main-tab-sheet.png`
- `screenshots/latest-home-screen-1.png`
- `screenshots/latest-main-navigation-editor.png`
- `screenshots/latest-calendar-screen.png`
- `screenshots/latest-home-screen-2.png`

These are evidence of the latest accepted UI, not source code.


<!-- END 00_README_RECOVERY_PACKAGE.md -->


---

<!-- BEGIN 01_RECOVERED_CURRENT_STATE.md -->

# Recovered Current State

## Recovery status

The original remote repository is reported deleted or inaccessible. A surviving local source tree exists, but its exact phase and Git state have not yet been audited. The owner describes it as a predecessor to the planned `1.6.0.1` release. It may contain the fully accepted Phase-4 FOSS debug runtime without the final version bump, or an earlier phase. This must be proven from source and Git evidence.

## Product identity

- Product: **Navori**
- Origin: branded and extended fork of `axiel7/AniHyou-android`
- Historical repository: `xnixjoyer/Navori`
- Historical upstream: `axiel7/AniHyou-android`
- Product branch: `main`
- Upstream mirror branch: `develop`
- Release application ID: `app.navori`
- Debug application ID: `app.navori.debug`
- Release launcher name: `Navori`
- Debug launcher name: `Navori Debug`
- Kotlin namespace intentionally retained: `com.axiel7.anihyou`
- AniList client ID intentionally retained: `8527`
- Phone callback intentionally retained: `anihyou://auth-response`
- Wear callback intentionally retained: `anihyou://wear-auth`

## Known historical upstream state

At the time of the completed phases:

- Fork `develop`: `259e81de6cd3ea51a488849bbd4777a2c3c7f342`
- Upstream `develop`: initially the same SHA.
- Later, upstream `develop` advanced by six commits after the successful Phase-4 dry run.
- Those six later commits were intentionally excluded from the first `1.6.0.1` release because they were not part of the owner's tested runtime.

A recovered repository must not assume `259e81...` is still current upstream. New upstream integration must be a separate tested task after local recovery.

## Phase history

### Phase 1 — Configurable primary navigation

Status in the lost remote history: **implemented, tested, merged, and manually accepted**.

Historical branch:

- `feature/configurable-main-navigation`

Historical merge commit:

- `390c186804b8b07a7c97770f0c35d2a2adf0bf03`

Core behavior:

- two to five visible main destinations;
- one persisted configuration shared by bottom navigation and navigation rail;
- reorder and visibility controls;
- safe normalization and migration;
- no auth/API/OAuth/version changes.

### Phase 2 — Date-based Calendar

Status in the lost remote history: **implemented, tested, merged, and manually accepted**.

Historical branch:

- `feature/date-based-calendar`

Historical merge commit:

- `72bd1a66d98651a7aabcb25655d17de85dfbdd5d`

Core behavior:

- Calendar is a real configurable top-level destination;
- Calendar does not become the app start page;
- opening Calendar initially focuses the current day;
- inclusive date range: today through today plus fourteen days;
- visible week: Monday through Sunday;
- left/right week navigation;
- selected date uses only a thin accent line;
- list is default; grid remains available and persisted;
- date counts and selected-day content use the same filter state;
- bottom navigation and navigation rail parity;
- nested Calendar route still has a back arrow;
- top-level Calendar route has no back arrow.

### Phase 3 — Modular main-tab shortcuts

Status in the lost remote history: **implemented, feedback-corrected, tested, manually accepted, and merged**.

Historical branch:

- `feature/modular-main-shortcuts`

Historical merge commit:

- `1ad1e2c7d34b87990dc16687afa7397aea42c787`

Core behavior:

- Home is mandatory and always the safe start/fallback;
- new/reset default hides Profile;
- migration preserves an already-visible Profile;
- Home top bar actions: Notifications, Settings, Account/Profile;
- account button opens the nested own-profile page even if the optional Profile main tab is hidden;
- compact tri-state Calendar filter;
- typed shortcut registry for Season, Home current lists, and Discover charts;
- dynamic destinations share the same bottom/rail model;
- exact duplicate parameterized shortcuts are removed;
- only one Season shortcut may exist;
- maximum five visible destinations remains;
- shortcut added at capacity is added hidden with feedback;
- original nested Home/Discover pages remain unchanged.

Available Home-list shortcuts:

- Airing / `Läuft gerade`
- Behind / `Ausstehende Folgen`
- Anime / `Am Schauen`
- Manga / `Am Lesen`
- Next Season / `Nächste Saison`

Available Discover shortcuts:

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

The owner reported two gaps after the first Phase-3 APK:

1. only season shortcuts appeared in the picker;
2. the Home account icon looked correct but did not navigate.

Both were fixed and the corrected APK was manually approved.

### Phase 4 — Stabilization and release preparation

Status in the lost remote history: **implemented, fully CI-validated, manually accepted, and merged**.

Historical branch:

- `feature/stabilization-release`

Accepted runtime head before release metadata:

- `39219598e3322e0af2dd25c30d2686906bee8386`

Final Phase-4 branch head:

- `1e4dd4c90191b30e3368d564be151261647e9fea`

Historical merge commit:

- `f2ef0220e9b93c929f302a76e2fe06caa777beac`

Core hardening:

- variant-safe notification target package;
- Navori worker notification branding;
- credential-bearing DataStore excluded from cloud backup and device transfer;
- dedicated enlarged borderless splash mark;
- launcher icons left unchanged;
- phone launcher shortcuts removed from common Wear resources;
- release/debug launcher shortcut package routing made variant-safe;
- Navori Wear strings isolated from shared upstream translation files;
- Wear compile SDK raised to 37 while target SDK remained 36;
- explicit Phase-1/2/final-schema migration tests;
- malformed/over-capacity navigation repair;
- visually distinguishable dynamic shortcut icons without extra TalkBack noise;
- FOSS/GMS/Wear/lint/R8 matrix.

Manual Phase-4 approval:

- The owner installed and approved the final FOSS debug build.
- The unsigned release candidate was not installable, which was expected because it was unsigned.

## Historical CI evidence

### Phase 2

- Final workflow run: `#80`
- Run ID: `30725227535`
- Result: success
- APK SHA-256: `5c611a3e6470e2b738ce2b3c017b7029e0d0989e55fe0a7f10bca87688674ca3`

### Phase 3 corrected feedback build

- Final workflow run: `#113`
- Run ID: `30739586981`
- Result: success
- Corrected APK SHA-256: `64d23daac59f159cec5bc6ad0406506a563c8ba8ca56a4b5b407b2b5971c0d5c`

### Phase 4 accepted runtime

- Workflow run: `#132`
- Run ID: `30742156860`
- Result: success
- FOSS debug SHA-256: `888787de732f4ccb0b65c4d188d81a7c203dd9a37990cc553c81aeed5f5e7a72`
- GMS debug SHA-256: `5de1c39863ee3bfe58016733bc1afe20433988bbe5ac488059ea633cd9b74e78`
- Wear debug SHA-256: `d39a9a09509c4a18125ac89a1e6428bd1aaf1000c87676cabb6191d7e37d0f11`
- Unsigned FOSS release-candidate SHA-256: `7ecb6f0df6d02a30cf8f986ef3548b6a352142782548b718ac68944ebf72ab75`

### Phase 4 explicit upstream dry run

- Temporary PR: `#5`
- Workflow run: `#133`
- Run ID: `30742799398`
- Result: success
- It used the then-synchronized upstream SHA `259e81...`.
- It was closed and never merged.

### Final release-metadata validation

- Workflow run: `#142`
- Run ID: `30749388676`
- Final PR head: `1e4dd4c...`
- Result: full verification success
- Release jobs were skipped on the PR, as intended, because signing/publishing was restricted to `main`.

## Intended first release

- Version name: `1.6.0.1`
- Android version code: `113`
- Intended tag: `v1.6.0.1`
- Intended release title: `Navori 1.6.0.1`
- Intended APK name: `Navori-1.6.0.1-foss-universal.apk`
- README: English
- Changelog and GitHub release notes: English

The signing secrets were empty when the first release was attempted. No verified signed public release is known to have been published. The permanent release key must never be regenerated after a real public release has been distributed, unless it is conclusively proven that no prior signed production APK exists.

## Exact next action

1. Preserve the surviving local tree and `.git` metadata.
2. Run the forensic audit.
3. Determine the highest fully present phase.
4. Create a new remote repository.
5. Push the preserved state without rewriting surviving history.
6. Restore missing phases only from the first absent acceptance criterion.
7. Rebuild and manually test an installable FOSS debug APK.
8. Reconstruct `1.6.0.1` release metadata only after the runtime matches the accepted screenshots and tests.
9. Configure a permanent signing key and publish a signed release.


<!-- END 01_RECOVERED_CURRENT_STATE.md -->


---

<!-- BEGIN 02_NEW_AI_BOOTSTRAP_PROMPT.md -->

# First Prompt for a New AI

Copy the entire prompt below into a new AI session. Attach or mount the surviving local Navori directory and this recovery package.

---

You are recovering the Android project **Navori** after its original GitHub repository was lost.

Your first responsibility is forensic preservation and state identification. Do not begin by rewriting the application and do not assume the local directory is old or incomplete.

## Sources you must read first

Read all Markdown files in the supplied `Navori-Recovery-Package`, in this order:

1. `00_README_RECOVERY_PACKAGE.md`
2. `01_RECOVERED_CURRENT_STATE.md`
3. `03_REPOSITORY_FORENSIC_AUDIT_PLAN.md`
4. `04_PRODUCT_MASTER_PLAN.md`
5. `06_PRODUCT_REQUIREMENTS_AND_DECISIONS.md`
6. `07_ARCHITECTURE_BUILD_AUTH_GUARDRAILS.md`
7. `08_VISUAL_ACCEPTANCE_REFERENCE.md`
8. `09_RELEASE_1_6_0_1_RECONSTRUCTION.md`
9. `10_ACCEPTANCE_AND_TEST_MATRIX.md`

Also inspect the supplied screenshots under `screenshots/`.

## Mandatory first stage: preserve the local source

Before modifying any file:

1. Print the absolute path of the supplied local project.
2. Record its file count, total size, modification timestamps, and whether `.git` exists.
3. Create:
   - a full source archive;
   - a separate `.git` archive when `.git` exists;
   - a Git bundle containing all refs when possible;
   - SHA-256 checksums for every recovery artifact.
4. Do not run destructive Git commands.
5. Do not use `git reset --hard`, `git clean`, force-push, rebase, garbage collection, or history rewriting.
6. Preserve untracked files, ignored files, worktree changes, reflogs, stashes, and local branches.

## Mandatory second stage: identify the exact surviving state

Inspect and report:

- current branch and HEAD;
- all local branches, remote-tracking branches, tags, refs, stashes, and reflogs;
- `git status --porcelain=v2`;
- merge-base and commit graph;
- `version.properties`;
- package IDs and launcher names;
- presence of Phase-1 navigation configuration;
- presence of Phase-2 date-based Calendar;
- presence of Phase-3 typed shortcuts, Home account fix, and all picker entries;
- presence of Phase-4 backup, notification, splash, Wear, migration, CI, README, changelog, and release changes;
- whether `README.md` and `CHANGELOG.md` are English;
- whether `version.properties` is still `1.6.0`/112 or already `1.6.0.1`/113;
- whether the build workflow can produce FOSS, GMS, Wear, lint, R8, and signed release artifacts.

Search for the exact source markers and paths listed in the recovery package. Do not infer implementation from filenames alone; inspect behavior and tests.

## Required evidence matrix

Create a table with one row for every acceptance criterion and these columns:

- Phase
- Requirement
- Source evidence
- Test evidence
- Screenshot/manual evidence
- Status: Present / Partial / Missing / Unknown
- Required action

At the end, state exactly one of these conclusions:

- `The local tree already contains the accepted Phase-4 runtime.`
- `The local tree contains Phase 3 but is missing part or all of Phase 4.`
- `The local tree contains Phase 2 but is missing Phase 3 and later work.`
- `The local tree contains only Phase 1 or an earlier base.`
- `The local tree cannot yet be classified because evidence is missing.`

## New repository upload

After preservation and classification, ask the owner for the exact new GitHub repository name and visibility if it was not supplied.

Then:

1. Create or initialize the new repository without adding an unrelated README/license.
2. Preserve existing Git history when valid.
3. When `.git` is missing, create a new initial recovery commit and clearly document that original commit identities were lost.
4. Create branches:
   - `main` for the recovered Navori product state;
   - `develop` only after fetching and verifying the chosen upstream `axiel7/AniHyou-android:develop`;
   - a dedicated recovery or feature branch for any missing implementation.
5. Push backup tags or recovery refs only when they do not expose secrets.
6. Never upload keystores, passwords, tokens, `.secrets`, local configuration, or private logs.

## Continuation rule

Do not restart all phases.

Continue from the first missing acceptance criterion shown by the evidence matrix. Preserve all working behavior already present. Reuse existing views, ViewModels, queries, routes, preferences, and tests instead of creating duplicate implementations.

Before each code change:

1. verify current `main`;
2. verify fork `develop`;
3. verify `axiel7/AniHyou-android:develop`;
4. compare behavior-sensitive files with upstream;
5. state which divergence is intentional.

## Build and release rule

The first target after recovery is an installable universal **FOSS debug APK** matching the accepted screenshots and behavior.

Only after manual owner approval:

- set version `1.6.0.1` and code `113` if absent;
- keep README and changelog in English;
- configure the permanent signing key;
- produce a signed universal FOSS release APK;
- verify it with `apksigner`;
- create tag `v1.6.0.1`;
- publish GitHub Release `Navori 1.6.0.1`.

## Required deliverables from your first response

Provide:

1. preservation artifacts and hashes;
2. Git/source forensic report;
3. phase evidence matrix;
4. exact highest recovered phase;
5. list of missing work only;
6. proposed new repository branch structure;
7. next implementation prompt tailored to the first missing criterion;
8. no code changes unless preservation and classification are complete.

---


<!-- END 02_NEW_AI_BOOTSTRAP_PROMPT.md -->


---

<!-- BEGIN 03_REPOSITORY_FORENSIC_AUDIT_PLAN.md -->

# Repository Forensic Audit Plan

## Goal

Determine whether the surviving local tree is:

- the accepted Phase-4 FOSS debug runtime;
- the final `1.6.0.1` release-preparation tree;
- a Phase-3 predecessor;
- an earlier phase;
- or a working tree with uncommitted later work.

## Preservation procedure

Run from the directory containing the project, not from inside a disposable clone.

### Inventory

Record:

```bash
pwd
date --iso-8601=seconds
find . -xdev -type f | wc -l
du -sh .
stat . .git 2>/dev/null || true
```

### Non-destructive source archive

```bash
tar --xattrs --acls --numeric-owner \
  -czf ../Navori-recovery-source-$(date +%Y%m%d-%H%M%S).tar.gz \
  .
```

### Separate Git metadata archive

When `.git` exists:

```bash
tar --xattrs --acls --numeric-owner \
  -czf ../Navori-recovery-dotgit-$(date +%Y%m%d-%H%M%S).tar.gz \
  .git
```

### Git bundle

When `.git` is valid:

```bash
git bundle create ../Navori-recovery-all-refs.bundle --all
git bundle verify ../Navori-recovery-all-refs.bundle
```

### Working-tree evidence

```bash
git status --porcelain=v2 --branch > ../status-porcelain-v2.txt
git diff --binary > ../worktree.patch
git diff --binary --cached > ../index.patch
git reflog show --all --date=iso > ../reflogs-all.txt
git show-ref > ../show-ref.txt
git branch -avv > ../branches.txt
git tag -n > ../tags.txt
git stash list > ../stashes.txt
git log --all --graph --decorate --oneline --date-order > ../commit-graph.txt
```

### Checksums

```bash
sha256sum \
  ../Navori-recovery-* \
  ../*.patch \
  ../*.txt \
  > ../SHA256SUMS.txt
```

Do not run cleanup or reset commands before these artifacts exist.

## Git classification

Inspect:

```bash
git rev-parse --show-toplevel
git rev-parse --is-inside-work-tree
git rev-parse HEAD
git symbolic-ref --short -q HEAD || true
git remote -v
git fsck --full --no-reflogs
git fsck --full
```

Look for historical known SHAs:

```bash
for sha in \
  390c186804b8b07a7c97770f0c35d2a2adf0bf03 \
  72bd1a66d98651a7aabcb25655d17de85dfbdd5d \
  1ad1e2c7d34b87990dc16687afa7397aea42c787 \
  39219598e3322e0af2dd25c30d2686906bee8386 \
  1e4dd4c90191b30e3368d564be151261647e9fea \
  f2ef0220e9b93c929f302a76e2fe06caa777beac
do
  git cat-file -e "${sha}^{commit}" 2>/dev/null \
    && echo "FOUND $sha" \
    || echo "MISSING $sha"
done
```

A missing SHA does not prove missing code. The local tree may have been copied without history or may contain squashed equivalents.

## Version audit

Inspect:

```bash
cat version.properties
grep -R --line-number --fixed-strings "1.6.0.1" .
grep -R --line-number --fixed-strings "code = 113" .
```

Expected final release metadata:

```text
code = 113
name = 1.6.0.1
wear_code = 200
wear_name = 1.2.0-wear
```

A tree with `1.6.0`/112 may still contain the complete accepted Phase-4 runtime because the version bump occurred after runtime approval.

## Phase marker audit

### Phase 1 markers

Search for:

- `MainNavigationConfig`
- `MainNavigationPreferencesRepository`
- configurable visible destinations;
- min 2 / max 5 rules;
- common bottom navigation and navigation rail configuration;
- navigation settings editor.

### Phase 2 markers

Search for:

- `CalendarDateRange.kt`
- `CalendarPreferencesRepository.kt`
- `CalendarMain`
- `CalendarHostViewModel`
- `CalendarListFilter`
- date range `today..today+14`;
- Monday-based week;
- list/grid persistence;
- timezone-aware day bounds.

### Phase 3 markers

Search for:

- `MainNavigationShortcut.kt`
- shortcut types `SEASON`, `CURRENT_LIST`, `CHART`;
- `SeasonShortcutMode`;
- `toOwnProfile()`;
- top-level chart and current-list route flags;
- picker categories Start and Discover;
- all five Home-list values;
- all nine chart values;
- singleton Season normalization;
- mandatory Home behavior.

### Phase 4 markers

Search for:

- `Phase4NavigationMigrationTest`
- `navori_splash_mark`
- `applicationContext.packageName` in notification worker;
- `navori_mark_24`;
- backup-rule exclusion for `datastore/default.preferences_pb`;
- phone shortcut XML under app resources rather than common resources;
- Wear compile SDK 37 and target SDK 36;
- FOSS/GMS/Wear/lint/R8 workflow steps;
- English `CHANGELOG.md`;
- English README release section;
- release gate and GitHub Release publication logic.

## Build audit

First inspect, then run:

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

Run the minified unsigned release candidate separately:

```bash
./gradlew :app:assembleFossRelease --no-daemon --stacktrace
```

The unsigned candidate is not expected to install. Its purpose is R8 and resource-shrinking validation.

## Source-to-screenshot comparison

Compare the running FOSS debug build with:

- main navigation editor;
- add-main-tab sheet;
- Home top bar;
- Home sections;
- five-destination bottom bar;
- Calendar week header and list;
- selected-day underline;
- account navigation;
- splash with no white rim.

## Required audit report

The report must include:

- backup artifact paths and SHA-256;
- HEAD and branch map;
- local modifications;
- version;
- phase matrix;
- build matrix;
- screenshot parity;
- secrets scan;
- exact missing work;
- safe upload plan.


<!-- END 03_REPOSITORY_FORENSIC_AUDIT_PLAN.md -->


---

<!-- BEGIN 04_PRODUCT_MASTER_PLAN.md -->

# Navori Product Master Plan

## Objective

Restore and maintain Navori as a branded, upstream-compatible Android AniList client with:

- configurable primary navigation;
- a date-based Calendar;
- modular Home and Discover shortcuts as true main tabs;
- safe migration and accessibility;
- FOSS, GMS, and Wear build coverage;
- an English public README and changelog;
- a signed first release `1.6.0.1`.

## Recovery phase — mandatory before product work

### Goal

Prove what survives in the local source tree and upload it safely to a new repository.

### Exit criteria

- immutable source and Git backups exist;
- phase evidence matrix is complete;
- new repository contains the preserved local state;
- `main` and `develop` branch policy is restored;
- secrets and signing files are excluded;
- the first missing product criterion is identified.

## Phase 1 — Configurable primary navigation

### Product outcome

Users can choose and reorder two to five visible main destinations. The same persisted configuration drives bottom navigation and navigation rail.

### Requirements

- visible minimum: 2;
- visible maximum: 5;
- reorder via drag handle;
- visibility toggles;
- safe normalization;
- stable technical IDs;
- persistence via one repository/DataStore owner;
- bottom/rail parity;
- preserve existing routes and behavior;
- no API/auth/version changes.

### Acceptance

- compact phone bottom bar;
- broad navigation rail;
- configuration survives restart;
- invalid/missing entries repair safely;
- owner manual approval.

## Phase 2 — Date-based Calendar

### Product outcome

Calendar becomes a configurable real main tab while the existing nested Calendar path remains available.

### Requirements

- app start remains Home or existing normal start, not Calendar;
- current day selected on open;
- inclusive range today through today plus 14 days;
- Monday-Sunday week;
- left/right week arrows;
- only dates inside the allowed range;
- selected date marked by thin accent underline only;
- list default;
- grid retained;
- list/grid mode persisted;
- same filter applied to counts and content;
- local timezone day bounds;
- nested route keeps back arrow;
- top-level route has no back arrow;
- bottom and rail parity;
- OLED-safe styling without new card outlines.

### Acceptance

- timezone and DST tests;
- count grouping tests;
- list/grid persistence test;
- compact/broad display check;
- owner manual approval.

## Phase 3 — Modular main-tab shortcuts

### Product outcome

Existing Home lists, Discover charts, and current/next season can be pinned as real main destinations without duplicating their data logic.

### Mandatory Home rules

- Home is always present and visible;
- Home is the startup destination;
- Home cannot be disabled;
- Home can be reordered;
- hidden/removed active destination falls back to Home;
- reset/new default hides Profile;
- migration preserves a visible Profile.

### Home top bar

Right-side actions, in order:

1. Notifications
2. Settings
3. Account/Profile

The account button opens the nested own-profile page, regardless of optional Profile main-tab visibility.

### Calendar tri-state filter

- `ALL`: neutral/gray;
- `ONLY_ON_LIST`: green/check;
- `HIDE_ON_LIST`: red/X.

One state must drive day counts, list, grid, and empty states.

### Shortcut registry

Stable typed entries:

- `SEASON`
- `CURRENT_LIST`
- `CHART`

Persist semantic parameters and technical IDs only. Never persist localized titles, concrete current year, or concrete current season.

### Home-list shortcuts

- Airing
- Behind
- Anime
- Manga
- Next Season

### Discover chart shortcuts

Anime:

- Top 100
- Popular
- Upcoming
- Airing
- Top Movies

Manga:

- Top 100
- Popular
- Upcoming
- Releasing

Season:

- Current Season
- Next Season

### Picker/editor behavior

- Plus button opens a scrollable Material bottom sheet;
- categories Start and Discover;
- exact option disappears after being added;
- unrelated options remain;
- multiple distinct Home-list/chart shortcuts may coexist;
- exact duplicates prohibited;
- only one Season shortcut;
- under five visible: added visible;
- at five visible: added hidden with Snackbar;
- reorder, hide, re-enable, remove;
- removing shortcut removes only navigation entry;
- bottom and rail use same registry.

### Reuse rule

- Home-list shortcuts reuse the existing full-list view and ViewModel.
- Discover chart shortcuts reuse the existing chart view and ViewModel.
- Season shortcut reuses the existing Season screen.
- Main-tab host removes back arrow.
- Original nested entry retains back arrow and original behavior.

### Acceptance

- all picker entries visible;
- account button works;
- exact duplicates prevented;
- all main/nested route contracts tested;
- owner manual approval.

## Phase 4 — Stabilization and release preparation

### Scope

No new major features.

### Navigation hardening

- Phase-1 and Phase-2 migrations into final schema;
- malformed/unknown entry repair;
- over-capacity repair;
- mandatory Home preservation;
- safe active-tab fallback;
- state restoration;
- keyboard/D-pad/TalkBack semantics;
- compact and broad insets;
- unique dynamic icon treatment.

### Calendar and Season hardening

- timezone/DST;
- day range;
- filter consistency;
- process recreation;
- current/next season rollover;
- year boundary;
- current season resolved at runtime;
- main/nested parity.

### Security and operational hardening

- token DataStore excluded from backup and transfer;
- notifications open the installed variant;
- Navori notification branding;
- release/debug shortcut package routing;
- dedicated borderless splash mark;
- Wear resource isolation;
- Wear compile SDK 37, target SDK 36;
- FOSS/GMS/Wear/lint/R8 CI;
- English README and changelog;
- signed release restricted to `main`;
- `apksigner` verification;
- no secrets in logs or repository.

### Acceptance

- full CI matrix green;
- upstream dry run green against chosen upstream point;
- FOSS debug manually approved;
- splash manually approved;
- release metadata reviewed.

## Release 1.6.0.1

### Metadata

- version name `1.6.0.1`;
- Android code `113`;
- tag `v1.6.0.1`;
- title `Navori 1.6.0.1`;
- asset `Navori-1.6.0.1-foss-universal.apk`;
- README and changelog English.

### Release gate

- permanent signing secrets configured;
- full CI green on `main`;
- signed FOSS universal APK;
- `apksigner verify --verbose` passes;
- release key securely backed up;
- English changelog section extracted into release notes;
- tag and GitHub Release created.

## Post-release

Only after `1.6.0.1` is safely published:

- fetch current upstream `develop`;
- compare with recovered `develop`;
- create tested integration branch;
- resolve conflicts by preserving upstream behavior and reapplying Navori as narrow patches;
- never copy old whole files over newer upstream files;
- produce a new test APK before merging.


<!-- END 04_PRODUCT_MASTER_PLAN.md -->


---

<!-- BEGIN 05_PHASE_PROMPTS.md -->

# Reconstructed Phase Prompts

These prompts are designed for copy/paste use. Always run the recovery/audit prompt first.

## Prompt 0 — Recover, audit, and upload the surviving local repository

Work on the surviving local Navori source tree.

Read the complete Navori recovery package first.

Before any modification:

1. create full source, `.git`, and all-refs bundle backups;
2. record SHA-256 hashes;
3. inspect branches, tags, reflogs, stashes, worktree changes, untracked files, and version metadata;
4. search for all Phase-1 through Phase-4 implementation markers;
5. build a requirement-by-requirement evidence matrix;
6. determine the highest complete phase and first missing criterion.

Do not reset, clean, rewrite, or discard local data.

After classification, ask for or use the owner's new GitHub repository destination. Upload the preserved state safely. Keep `main` as product branch. Create `develop` only as a verified mirror of current `axiel7/AniHyou-android:develop`. Never put Navori-only changes on `develop`.

Do not start implementation until the forensic report and upload are complete.

Deliver:

- backup artifacts and hashes;
- exact local Git state;
- phase evidence matrix;
- build results;
- screenshot parity report;
- uploaded branch model;
- next prompt tailored only to missing work.

## Prompt 1 — Implement configurable primary navigation

Execute only when Phase 1 is proven missing or partial.

Implement configurable primary navigation on a new feature branch.

Requirements:

- users choose two to five visible main destinations;
- reorder via drag handle;
- persist stable technical IDs;
- one normalized configuration drives bottom navigation and navigation rail;
- handle unknown, duplicate, missing, and old entries safely;
- preserve routes, auth, API, OAuth, application IDs, and version;
- add tests for normalization, limits, persistence, and migration;
- validate compact bottom navigation and broad navigation rail.

Update the AI handoff and produce an installable universal FOSS debug APK.

Do not begin Phase 2 until the owner approves Phase 1.

## Prompt 2 — Implement the date-based Calendar main tab

Execute only when Phase 1 is accepted and Phase 2 is missing or partial.

Implement Calendar as a configurable true main destination without removing or redesigning the existing nested Calendar page.

Contract:

- Calendar is not the app start page;
- opening Calendar initially focuses today;
- allowed inclusive range is today through today plus fourteen days;
- week is Monday through Sunday;
- navigate weeks with left/right arrows;
- selected day uses only a thin accent underline;
- list is default;
- grid remains and mode is persisted;
- day counts and selected-day content use the same filter;
- use local timezone day bounds and test DST;
- nested route keeps back arrow;
- main route has no back arrow;
- both bottom navigation and rail work;
- preserve existing item components and OLED-safe design.

Run:

- app FOSS debug assembly;
- app unit tests;
- model tests;
- domain tests;
- calendar tests.

Produce an installable universal FOSS debug APK and update the handoff.

Do not begin Phase 3 until manual owner approval.

## Prompt 3 — Implement modular main-tab shortcuts

Execute only when Phase 2 is accepted and Phase 3 is missing or partial.

Implement a typed modular shortcut registry and picker without duplicating existing feature logic.

Mandatory Home:

- Home is always visible;
- Home cannot be disabled;
- Home is startup and fallback;
- Home may be reordered;
- new/reset default hides Profile;
- preserve an already-visible Profile during migration.

Home top bar:

- Notifications;
- Settings;
- Account/Profile;
- Account opens nested own-profile page even when Profile main tab is hidden.

Calendar:

- replace the long list filter with compact tri-state menu;
- all, only-on-list, hide-on-list;
- same state for counts, list, grid, empty state.

Shortcut types:

- Season;
- Home current list;
- Discover chart.

Home shortcuts:

- Airing;
- Behind;
- Anime;
- Manga;
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

Picker:

- Material bottom sheet;
- Start and Discover categories;
- exact options disappear only after being added;
- multiple different Home-list/chart shortcuts allowed;
- exact duplicates prohibited;
- only one Season shortcut;
- add visible below five;
- add hidden with feedback at five;
- reorder, hide, enable, remove.

Reuse the existing Home full-list, chart, and Season views/ViewModels. Main hosts have no back arrow; original nested entry points remain unchanged with back navigation.

Test model codec, persistence, exact duplicates, singleton Season, route classification, account navigation, Home protection, and bottom/rail parity.

Produce a corrected universal FOSS debug APK and require owner approval.

## Prompt 4 — Stabilize and prepare the first release

Execute only when Phases 1–3 are accepted and Phase 4 is missing or partial.

Do not add major features.

Harden:

- navigation migration from Phase 1 and Phase 2;
- typed shortcut migration;
- malformed and over-capacity repair;
- mandatory Home and safe fallback;
- TalkBack, keyboard/D-pad, touch targets, compact/broad layouts;
- list/grid and navigation state restoration;
- Calendar timezone, DST, filter consistency, date boundaries;
- Season current/next rollover and year boundaries;
- Light, Dark, OLED;
- FOSS, GMS, and Wear;
- notification variant routing;
- notification branding;
- token backup exclusion;
- phone/Wear resource boundaries;
- dedicated borderless enlarged splash mark;
- upstream compatibility.

CI must cover:

- `:app:assembleFossDebug`
- `:app:assembleGmsDebug`
- `:app:testFossDebugUnitTest`
- `:app:testGmsDebugUnitTest`
- `:core:model:testDebugUnitTest`
- `:core:domain:testDebugUnitTest`
- `:feature:calendar:testDebugUnitTest`
- `:wearos:assembleDebug`
- `:app:lintFossDebug`
- `:app:lintGmsDebug`
- `:app:assembleFossRelease`

The unsigned release candidate is for R8 validation only and need not install.

Update README and changelog in English. Produce FOSS/GMS debug, Wear debug, and unsigned release-candidate artifacts. Require owner testing of the FOSS debug build.

## Prompt 5 — Reconstruct and publish Navori 1.6.0.1

Execute only after the recovered Phase-4 FOSS debug build is manually approved.

Verify that no prior public production APK exists with a different signing identity.

Set:

- version name `1.6.0.1`;
- Android version code `113`;
- tag `v1.6.0.1`;
- release title `Navori 1.6.0.1`;
- APK name `Navori-1.6.0.1-foss-universal.apk`.

Ensure README, changelog, and release notes are English.

Configure permanent repository secrets:

- `KEYSTORE_FILE`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Build only from `main`, after full CI. Verify the signed APK with `apksigner`. Create the tag and GitHub Release. Attach the signed universal FOSS APK. Record SHA-256, certificate fingerprint, workflow run, tag, release URL, and backup location of the permanent signing key.

Never commit or print the keystore or passwords.


<!-- END 05_PHASE_PROMPTS.md -->


---

<!-- BEGIN 06_PRODUCT_REQUIREMENTS_AND_DECISIONS.md -->

# Product Requirements and Durable Decisions

## Navigation

- Home/Start is mandatory.
- Home is the normal app start page.
- Calendar must never become the automatic start page.
- There must be at least two and at most five visible main destinations.
- Bottom navigation and navigation rail must use the same configuration.
- Reordering one must affect the other.
- When an active destination is hidden or removed, navigate safely to Home.
- Profile is optional and hidden in new/reset defaults.
- Migration must preserve a Profile tab that was already visible.
- Dynamic shortcuts are first-class destinations, not ad-hoc special cases in bottom/rail code.
- Persist technical IDs and semantic parameters only.
- Never persist localized labels, current season names, or years.

## Home

The accepted Home layout keeps the existing two tabs:

- `Aktuell`
- `Aktivität`

Top-right actions, in order:

1. Notifications with badge
2. Settings
3. Account/Profile

The account action must open the nested own-profile screen. It must work even when the optional Profile main tab is hidden.

Existing Home sections include:

- Airing / `Läuft gerade`
- Behind / `Ausstehende Folgen`
- Watching / `Am Schauen`
- Reading / `Am Lesen`
- Next Season / `Nächste Saison`

A Home-list shortcut opens the existing full list as a main tab. It must preserve media details, +1, edit, login handling, and refresh behavior.

## Calendar

- Inclusive date boundary: today through today + 14 days.
- Initially selected date: today.
- Week starts Monday and ends Sunday.
- Week navigation uses left/right arrows.
- Selection indicator is a thin accent line only.
- No selected pill, border, card, or outlined container.
- List mode is default.
- Grid remains available.
- Mode is persisted.
- The list can show poster, title, episode, local airing time, score, and list status using existing components.
- Day counts and content must share the same filter.
- Local time and timezone must be respected.
- Handle DST.
- OLED design must avoid unnecessary outlined windows/cards.
- Main route has no back arrow.
- Nested route retains back arrow.

## Calendar filter

Three states:

- all titles: neutral/gray;
- only titles on my list: green with check;
- hide titles on my list: red with X.

Use the existing nullable preference contract when present:

- `null` = all;
- `true` = only on list;
- `false` = hide on list.

## Shortcut picker

Accepted title in German UI:

- `Haupttab hinzufügen`

Accepted categories:

- `Start`
- `Entdecken`

Home options:

- `Läuft gerade`
- `Ausstehende Folgen`
- `Am Schauen`
- `Am Lesen`
- `Nächste Saison`

Discover options:

- `Aktuelle Saison`
- `Nächste Saison`
- `Top 100 Anime`
- popular Anime
- upcoming Anime
- airing Anime
- top movies
- `Top 100 Manga`
- popular Manga
- upcoming Manga
- releasing Manga

The picker is scrollable. Each row includes icon, title, and concise explanation.

At fewer than five visible destinations, a newly added shortcut becomes visible. At five visible destinations, it is added hidden and the user receives feedback.

## Season shortcut

- Exactly one Season shortcut may exist.
- Stored mode: current or next.
- Concrete season and year are resolved at runtime.
- Bottom/rail compact label shows localized season name only.
- Screen title shows season plus year.
- Resolve again on app resume.
- ViewModel identity includes season and year.
- Existing Discover Season page remains nested and visually unchanged.
- The main host has no back arrow.

## Icons and accessibility

- Static destinations and dynamic shortcuts should not become visually ambiguous.
- Dynamic Home lists and charts may use small decorative marks.
- Decorative marks must not add separate TalkBack content.
- Full localized destination label is the accessible name.
- Drag-and-drop rows require accessible sort actions.
- Material touch targets must be preserved.
- Bottom and rail labels must remain readable.

## Splash

Accepted correction:

- remove the white/light rim visible around the logo;
- show only the Navori mark;
- make the mark larger in the Android system splash;
- use a dedicated transparent splash asset;
- do not alter the normal launcher icon solely to fix splash.

## Branding and localization

- Visible app branding is Navori.
- README and changelog for the public release are English.
- The application UI may remain localized, including German.
- Avoid editing all shared upstream translation files merely to replace the app name.
- Navori-specific Wear text should live in Wear-local overrides where practical.

## Release

- First intended public version: `1.6.0.1`.
- Android code: `113`.
- FOSS universal APK is the production release artifact.
- Unsigned release candidate is not a production install.
- Permanent signing key must be backed up.
- Future updates must use the same key after first public release.


<!-- END 06_PRODUCT_REQUIREMENTS_AND_DECISIONS.md -->


---

<!-- BEGIN 07_ARCHITECTURE_BUILD_AUTH_GUARDRAILS.md -->

# Architecture, Build, Authentication, and Upstream Guardrails

## Branch model

- `main`: Navori product branch.
- `develop`: exact mirror of selected upstream `axiel7/AniHyou-android:develop`.
- Navori-only work: feature branches and reviewed pull requests.
- Upstream updates: tested integration branch and pull request.
- Never commit Navori product work directly to `develop`.

## Upstream principle

Navori is a maintainable extension layer, not a wholesale rewrite.

Preserve upstream behavior by default for:

- AniList API;
- OAuth;
- token parsing and storage;
- GraphQL;
- MAL metadata;
- translations unrelated to Navori-specific visible text;
- versioning unless owner explicitly approves;
- module boundaries;
- Kotlin namespace.

When resolving an upstream conflict:

1. classify the Navori change as identity, operations, or approved additive product behavior;
2. keep upstream behavior for auth/network/architecture;
3. reapply the smallest Navori patch;
4. compare the resolved file with both parents;
5. build and test before merge;
6. never replace a new upstream whole file with an old Navori copy.

## Identity

- Release ID: `app.navori`
- Debug ID: `app.navori.debug`
- Release name: `Navori`
- Debug name: `Navori Debug`
- Internal namespace: `com.axiel7.anihyou`

## Authentication

Expected unchanged AniList contract:

- Client ID: `8527`
- Authorization endpoint: `https://anilist.co/api/v2/oauth/authorize`
- Flow: implicit token
- Phone callback: `anihyou://auth-response`
- Wear callback: `anihyou://wear-auth`
- GraphQL endpoint: `https://graphql.anilist.co`

Do not invent a `navori://` callback while using the old client registration.

Known side-by-side limitation:

Navori and upstream AniHyou can share the same callback scheme. Android may ask which application should open it. A deterministic independent callback requires a separately controlled AniList OAuth client.

## Token and backup security

The DataStore containing credentials must be excluded from:

- Android cloud backup;
- device-to-device transfer.

The recorded path was:

```text
datastore/default.preferences_pb
```

Confirm the actual local implementation before relying on this path.

## Notification hardening

Notification pending intents must use the installed package dynamically, for example the equivalent of:

```text
applicationContext.packageName
```

Do not hardcode only the release ID because debug notifications would open the wrong package or fail.

Use Navori notification branding and the installed variant's app name.

## Resource boundaries

- Phone launcher shortcuts belong in the phone app module.
- They must not leak into Wear resources.
- Release and debug shortcut target packages must resolve per variant.
- Navori-specific Wear text should be in Wear resource overrides.
- Common upstream resources should stay close to upstream.

## Splash and launcher

- Launcher/adaptive icon and system splash are separate concerns.
- Use a dedicated transparent enlarged splash mark.
- Keep launcher assets unchanged unless the owner requests a launcher redesign.

## Expected important source areas

These paths are reconstructed from the historical implementation and must be confirmed:

### Navigation model and persistence

- `core/model/.../navigation/MainNavigationConfig.kt`
- `core/model/.../navigation/MainNavigationShortcut.kt`
- `core/domain/.../MainNavigationPreferencesRepository.kt`

### Shared navigation

- `core/ui/.../BottomDestination.kt`
- `core/ui/.../navigation/Routes.kt`
- `core/ui/.../navigation/NavActionManager.kt`
- `core/ui/.../navigation/NavigationState.kt`

### App shell

- `app/.../MainActivity.kt`
- `app/.../MainNavigation.kt`
- `app/.../MainViewModel.kt`
- `app/.../composables/MainBottomNavBar.kt`
- `app/.../composables/MainNavigationRail.kt`

### Calendar

- `feature/calendar/.../CalendarDateRange.kt`
- `feature/calendar/.../CalendarListFilter.kt`
- `feature/calendar/.../CalendarHostViewModel.kt`
- `feature/calendar/.../CalendarViewModel.kt`
- `feature/calendar/.../CalendarView.kt`
- `core/domain/.../CalendarPreferencesRepository.kt`

### Home, charts, season

- `feature/home/.../HomeView.kt`
- `feature/home/.../current/fulllist/CurrentFullListView.kt`
- `feature/explore/.../charts/MediaChartListView.kt`
- `feature/explore/.../season/SeasonAnimeView.kt`

### Navigation settings

- `feature/settings/.../navigation/MainNavigationSettingsView.kt`
- `feature/settings/.../navigation/MainNavigationSettingsViewModel.kt`

### Phase-4 operational areas

- `.github/workflows/build-upload-android.yml`
- `core/resources/.../xml/backup_rules.xml`
- `core/resources/.../xml/data_extraction_rules.xml`
- `feature/worker/.../NotificationWorker.kt`
- `core/resources/.../values/themes.xml`
- `gradle/libs.versions.toml`
- `docs/BUILDING.md`
- `CHANGELOG.md`
- `README.md`
- `version.properties`

## Build matrix

Expected hardened verification:

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

Minified release candidate:

```bash
./gradlew :app:assembleFossRelease --no-daemon --stacktrace
```

## Release signing

Required GitHub repository secrets:

- `KEYSTORE_FILE`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

Rules:

- signed production build only from `main`;
- full verification must succeed first;
- verify APKs with `apksigner`;
- never log secrets;
- remove temporary decoded keystore;
- do not regenerate the permanent key after a public release;
- record public certificate fingerprints and secure backup locations.


<!-- END 07_ARCHITECTURE_BUILD_AUTH_GUARDRAILS.md -->


---

<!-- BEGIN 08_VISUAL_ACCEPTANCE_REFERENCE.md -->

# Visual Acceptance Reference

This document describes what is directly visible in the owner's latest supplied screenshots.

## Screenshot: add main tab sheet

File:

- `screenshots/latest-add-main-tab-sheet.png`

Visible behavior:

- modal bottom sheet with drag handle;
- title `Haupttab hinzufügen`;
- section `Start`;
- entries:
  - `Läuft gerade`
  - `Ausstehende Folgen`
  - `Am Schauen`
  - `Am Lesen`
  - `Nächste Saison`
- section `Entdecken`;
- visible entries include:
  - `Aktuelle Saison`
  - `Nächste Saison`
  - `Top 100 Anime`
- rows contain icon, title, and explanatory text;
- sheet is scrollable;
- dark/OLED presentation;
- no oversized outlined cards around each item.

The source must also expose the remaining accepted Anime and Manga charts below the visible viewport.

## Screenshot: Home

Files:

- `screenshots/latest-home-screen-1.png`
- `screenshots/latest-home-screen-2.png`

Visible behavior:

- title `Start`;
- top-right:
  - notification bell with badge;
  - settings gear;
  - account icon;
- tabs:
  - `Aktuell`
  - `Aktivität`
- selected tab uses purple underline;
- Home sections include:
  - `Läuft gerade`
  - `Ausstehende Folgen`
  - `Am Lesen` visible lower down;
- horizontal media carousels;
- +1 controls;
- bottom navigation with exactly five visible destinations:
  - Start
  - Anime
  - Manga
  - Erkunden
  - Kalender
- selected Home destination uses a filled rounded indicator behind the icon;
- dark/OLED background and standard Navori/AniHyou typography.

The account icon must be interactive and open the own-profile nested page.

## Screenshot: main navigation editor

File:

- `screenshots/latest-main-navigation-editor.png`

Visible behavior:

- title `Hauptnavigation anpassen`;
- top-right reset action `Zurücksetzen`;
- instructions for selecting and dragging;
- highlighted constraint text:
  - two to five visible targets;
  - Start is mandatory;
- rows:
  - Start, marked `Verpflichtend`;
  - Anime;
  - Manga;
  - Profil;
  - Erkunden;
  - Kalender;
- switches for visibility;
- drag handles;
- Start switch visually locked/disabled;
- floating plus button in lower-right;
- dark/OLED styling;
- no card window surrounding the full editor.

## Screenshot: Calendar

File:

- `screenshots/latest-calendar-screen.png`

Visible behavior:

- title `Kalender`;
- top-right grid/list control and compact filter icon;
- week title, for example `3. Aug – 9. Aug`;
- left/right week arrows;
- Monday through Sunday columns;
- day number and count per date;
- selected date indicated by thin purple line;
- list items show:
  - poster;
  - title;
  - episode and local airing time;
  - score indicator;
- bottom navigation remains visible;
- selected Calendar destination uses filled rounded icon indicator;
- content respects bottom navigation inset;
- dark/OLED appearance without new outlined cards.

## Splash reference from earlier accepted feedback

The owner previously supplied a splash screenshot showing a white/light rim around the logo. The accepted fix was:

- use only the Navori mark;
- remove the white rim;
- enlarge the mark;
- use a dedicated transparent splash drawable;
- leave launcher icon unchanged.

That earlier splash image is not included in the five current files, but the decision is conversation-verified.

## Manual screenshot parity method

For every recovered build:

1. use the same language and OLED/dark theme;
2. capture the same screens;
3. compare:
   - spacing;
   - title/action placement;
   - bottom bar order;
   - selected indicators;
   - row labels;
   - counts;
   - absence of unintended white borders;
4. record deviations as:
   - functional;
   - visual;
   - data-dependent;
   - acceptable platform difference.


<!-- END 08_VISUAL_ACCEPTANCE_REFERENCE.md -->


---

<!-- BEGIN 09_RELEASE_1_6_0_1_RECONSTRUCTION.md -->

# Navori 1.6.0.1 Release Reconstruction

## Intended release identity

```text
Version name: 1.6.0.1
Android version code: 113
Wear version name: 1.2.0-wear
Wear version code: 200
Tag: v1.6.0.1
Release title: Navori 1.6.0.1
Release APK: Navori-1.6.0.1-foss-universal.apk
Application ID: app.navori
```

## Important distinction

The accepted Phase-4 FOSS debug APK was installable and manually approved.

The unsigned FOSS release candidate was not installable. This was expected. Its purpose was only:

- R8 validation;
- resource shrinking;
- release resource validation.

Do not treat unsigned-candidate installation failure as a product bug.

## English public documentation

The final release preparation required:

- `README.md` in English;
- `CHANGELOG.md` in English;
- GitHub release notes in English.

Application UI localization, including German screenshots, is not in conflict with this requirement.

## Expected changelog content

The `1.6.0.1` section should cover:

### Added

- configurable main navigation;
- bottom navigation and navigation rail parity;
- date-based Calendar;
- list/grid mode;
- modular Home-list, Discover-chart, and Season main tabs;
- main-tab picker;
- English release documentation.

### Changed

- Home is mandatory startup/fallback;
- Profile hidden by default but migration-safe;
- Home top-bar actions;
- compact Calendar tri-state filter;
- dynamic season labels;
- larger borderless splash mark;
- expanded CI matrix.

### Fixed

- Home account button no longer silently falls back to Home;
- notification variant routing;
- token backup exclusion;
- phone/Wear resource leakage;
- malformed navigation migration;
- dynamic icon ambiguity.

### Security

- DataStore containing auth token excluded from cloud backup and device transfer;
- signing secrets kept outside repository;
- signed releases restricted to `main`.

## Signing identity recovery decision

Before generating a new permanent key, determine whether any signed `app.navori` production APK was ever distributed.

Check:

- old downloads;
- phone installed package;
- APK backups;
- GitHub Actions downloads;
- local build outputs;
- release folders;
- messaging uploads;
- certificate fingerprints.

When a signed production APK exists, future releases must use the same key.

When it is conclusively proven that no signed production APK was published or distributed, a new permanent key may be created and becomes the identity for all future releases.

## Required permanent secrets

```text
KEYSTORE_FILE
KEYSTORE_PASSWORD
KEY_ALIAS
KEY_PASSWORD
```

`KEYSTORE_FILE` is the Base64-encoded keystore contents.

## Release workflow requirements

1. Trigger from `main`.
2. Run the complete verification matrix.
3. Verify signing secrets exist.
4. Decode keystore to a permission-restricted temporary file.
5. Build `:app:assembleFossRelease`.
6. Verify every generated APK with `apksigner`.
7. Stage the universal APK with release-friendly filename.
8. Extract English notes from `CHANGELOG.md`.
9. Create tag `v1.6.0.1`.
10. Create GitHub Release `Navori 1.6.0.1`.
11. Attach signed APK.
12. Record SHA-256 and certificate fingerprints.
13. Delete the temporary decoded keystore.

## Final release evidence record

After publishing, update the handoff with:

- new repository URL;
- `main` commit;
- tag commit;
- workflow run ID;
- release URL;
- APK filename and size;
- APK SHA-256;
- APK signing certificate SHA-256;
- whether install/upgrade was tested;
- secure backup confirmation for the permanent keystore;
- exact upstream SHA excluded or included.


<!-- END 09_RELEASE_1_6_0_1_RECONSTRUCTION.md -->


---

<!-- BEGIN 10_ACCEPTANCE_AND_TEST_MATRIX.md -->

# Acceptance and Test Matrix

## Recovery acceptance

- [ ] Full local source archive exists.
- [ ] Separate `.git` archive exists when applicable.
- [ ] All-refs Git bundle exists when applicable.
- [ ] SHA-256 manifest exists.
- [ ] Worktree, index, reflog, refs, tags, stashes, and branches captured.
- [ ] No destructive command was run before preservation.
- [ ] New repository upload contains no secrets.

## Phase 1

### Automated

- [ ] default configuration;
- [ ] min two visible;
- [ ] max five visible;
- [ ] reorder persists;
- [ ] visibility persists;
- [ ] unknown IDs dropped safely;
- [ ] duplicates normalized;
- [ ] bottom/rail same order;
- [ ] old configuration migration.

### Manual

- [ ] compact bottom navigation;
- [ ] broad navigation rail;
- [ ] restart persistence;
- [ ] active-tab fallback;
- [ ] no clipped labels or inaccessible controls.

## Phase 2

### Automated

- [ ] today through today+14 inclusive;
- [ ] Monday week start;
- [ ] final week boundary;
- [ ] timezone-aware start/end;
- [ ] DST transition;
- [ ] local timestamp to date grouping;
- [ ] daily counts;
- [ ] list/grid persistence;
- [ ] nested/main route classification;
- [ ] Calendar excluded from startup selection.

### Manual

- [ ] today focused on open;
- [ ] thin underline only;
- [ ] week arrows;
- [ ] list default;
- [ ] grid works;
- [ ] compact filter;
- [ ] counts/content same filter;
- [ ] bottom bar inset;
- [ ] rail inset;
- [ ] Light/Dark/OLED.

## Phase 3

### Automated

- [ ] Home mandatory;
- [ ] Home startup/fallback;
- [ ] reset hides Profile;
- [ ] migration preserves visible Profile;
- [ ] account action opens nested own profile;
- [ ] Season singleton;
- [ ] current/next season round trip;
- [ ] year boundary;
- [ ] all Home-list types registered;
- [ ] all chart types registered;
- [ ] distinct parameterized shortcuts coexist;
- [ ] exact duplicate removed;
- [ ] exact shortcut removal preserves siblings;
- [ ] add hidden at five visible;
- [ ] top-level chart route classified as main;
- [ ] nested Discover chart remains nested;
- [ ] top-level Home full list classified as main;
- [ ] nested Home full list remains nested;
- [ ] calendar filter has all three states;
- [ ] filter persistence.

### Manual

- [ ] Home notification button;
- [ ] Home settings button;
- [ ] Home account button;
- [ ] picker shows all five Start options;
- [ ] picker shows both season modes and all charts;
- [ ] exact option disappears after add;
- [ ] unrelated options remain;
- [ ] only one Season shortcut;
- [ ] multiple charts/lists coexist;
- [ ] capacity feedback;
- [ ] reorder/hide/re-enable/remove;
- [ ] main pages have no back arrow;
- [ ] original nested pages keep back arrow;
- [ ] details/+1/edit/rank/pagination preserved;
- [ ] bottom/rail parity.

## Phase 4

### Automated

- [ ] Phase-1 migration;
- [ ] Phase-2 migration;
- [ ] final schema round trip;
- [ ] malformed over-capacity repair;
- [ ] unknown typed parameter repair;
- [ ] FOSS debug;
- [ ] GMS debug;
- [ ] FOSS app tests;
- [ ] GMS app tests;
- [ ] model tests;
- [ ] domain tests;
- [ ] calendar tests;
- [ ] Wear debug;
- [ ] FOSS lint;
- [ ] GMS lint;
- [ ] minified FOSS release candidate;
- [ ] backup rule grep/contract;
- [ ] notification package contract;
- [ ] splash asset contract;
- [ ] Wear SDK contract.

### Manual

- [ ] accepted Home screenshots;
- [ ] accepted navigation editor screenshot;
- [ ] accepted add-tab sheet screenshot;
- [ ] accepted Calendar screenshot;
- [ ] borderless enlarged splash;
- [ ] compact display;
- [ ] broad display/rail;
- [ ] Light;
- [ ] Dark;
- [ ] OLED;
- [ ] TalkBack labels;
- [ ] drag accessibility actions;
- [ ] keyboard/D-pad where applicable;
- [ ] process recreation;
- [ ] app restart;
- [ ] login and account navigation;
- [ ] notification opens correct variant;
- [ ] FOSS debug installs;
- [ ] GMS debug installs when intended;
- [ ] Wear debug builds/installs on supported device.

## Release 1.6.0.1

- [ ] `version.properties` = name `1.6.0.1`, code `113`;
- [ ] README English;
- [ ] changelog English;
- [ ] permanent signing key identified or generated safely;
- [ ] key backed up in two secure locations;
- [ ] GitHub secrets configured;
- [ ] full `main` CI green;
- [ ] signed universal FOSS APK built;
- [ ] `apksigner verify --verbose` passes;
- [ ] tag `v1.6.0.1`;
- [ ] GitHub Release title `Navori 1.6.0.1`;
- [ ] APK attached with expected filename;
- [ ] APK SHA-256 recorded;
- [ ] certificate SHA-256 recorded;
- [ ] clean install tested;
- [ ] upgrade path tested when a prior signed build exists.


<!-- END 10_ACCEPTANCE_AND_TEST_MATRIX.md -->


---

<!-- BEGIN 11_COPY_PASTE_PROMPTS_ONLY.md -->

# Copy-Paste Prompts Only

## A. Audit the local tree and upload it safely

Use `02_NEW_AI_BOOTSTRAP_PROMPT.md` in full.

## B. Continue from the first missing phase

After the audit, use exactly one of the prompts in `05_PHASE_PROMPTS.md`.

Do not use a later prompt until the previous phase is proven complete and accepted.

## C. Final one-line continuation instruction

After the audit report, the owner can send:

> Continue from the first criterion marked Missing or Partial in the evidence matrix. Preserve every criterion marked Present. Work on a new feature branch, run the full relevant CI matrix, produce an installable universal FOSS debug APK, update the AI handoff, and stop for manual approval before advancing to the next phase.

## D. Release instruction

After final FOSS debug approval, the owner can send:

> Reconstruct the first public release as Navori 1.6.0.1 with Android code 113. Keep README, CHANGELOG, and release notes in English. Build and publish only from main with the permanent signing key, verify the APK with apksigner, create tag v1.6.0.1, attach Navori-1.6.0.1-foss-universal.apk, and record the APK and certificate SHA-256 values in the handoff.


<!-- END 11_COPY_PASTE_PROMPTS_ONLY.md -->
