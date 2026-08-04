# Historical Source Backup Audit

> This audit describes the uploaded backup under its former project name. The new product name is **Kiyori**.

# Navori Backup Phase Audit

## Final classification

**The backup is a pre-Phase-1 infrastructure baseline.**

It contains the upstream AniHyou source at commit `259e81de6cd3ea51a488849bbd4777a2c3c7f342` plus nine Navori-oriented build, CI, signing-documentation, and NixOS commits, ending at:

- branch: `navori/develop`
- HEAD: `476ad447217ecae2b7c7ae710f7981ca55d9a003`
- version: `1.6.0`
- Android code: `112`

It does **not** contain the configurable navigation, date-based Calendar, modular shortcut registry, Phase-4 stabilization, accepted Navori UI, or `1.6.0.1` release metadata.

ZIP SHA-256:

```text
8e2142d0581e2eeadb2a26262261d4cca11dacde5cdd1ef6135d6a85391750b1
```

## Git evidence

Present refs:

- local `navori/develop` → `476ad447...`
- local `stable` → `11aa1513...`
- remote-tracking `origin/develop` → `259e81de...`
- remote-tracking `origin/navori/develop` → `476ad447...`
- remote-tracking `origin/stable` → `11aa1513...`

No Phase-1 through Phase-4 historical commits are present in the object database:

- `390c186...` — missing
- `72bd1a66...` — missing
- `1ad1e2c...` — missing
- `39219598...` — missing
- `1e4dd4c...` — missing
- `f2ef0220...` — missing

`git fsck --full --no-reflogs --unreachable` found no hidden or unreachable later commits.

The worktree has one staged modification:

- `.github/workflows/build-upload-android.yml`
- adds `--channel=3` to two Android SDK installation commands

This staged change is build infrastructure only and does not advance a product phase.

## What is preserved

The branch contains nine commits after upstream `259e81de...`:

1. manual and fork-safe release build;
2. manual and fork-safe Crowdin workflow;
3. hardened Android CI and signed-release workflow;
4. least-privilege Crowdin authentication;
5. signing-material ignore rules;
6. reproducible NixOS Android shell;
7. NixOS build and signing guide;
8. explicit Android SDK tool paths;
9. Android Build Tools exposed in the Nix shell.

Preserved useful files include:

- `.github/workflows/build-upload-android.yml`
- `.github/workflows/crowdin.yml`
- `.gitignore`
- `docs/BUILDING.md`
- `shell.nix`

The workflow can build/test a FOSS debug APK and manually build a signed FOSS release artifact from `navori/develop`, provided signing secrets exist.

## Branding state

The product branding is not implemented in this backup:

- application ID is still `com.axiel7.anihyou`;
- release launcher name is still `AniHyou`;
- debug launcher name is still `AniHyou Debug`;
- README is still the upstream AniHyou README;
- package constant is still `com.axiel7.anihyou`;
- no Navori launcher/splash assets are present;
- no `AGENTS.md`, AI context directory, or English Navori changelog is present.

The word “Navori” appears only in build/signing documentation, workflow text, and the Nix shell banner.

## Phase evidence matrix

| Phase | Result | Evidence |
|---|---|---|
| Phase 1 — Configurable main navigation | **Missing** | Navigation is a fixed five-item `BottomDestination.values` list. No navigation config model, repository, editor, migration, or shared dynamic registry exists. |
| Phase 2 — Date-based Calendar | **Missing** | Calendar is the original nested weekday pager with a back arrow and adaptive grid. No `CalendarMain`, date-range model, Monday week strip, today+14 boundary, list mode, or persisted list/grid preference exists. |
| Phase 3 — Modular main-tab shortcuts | **Missing** | No typed shortcut model, add-main-tab sheet, Home/Discover shortcut routes, current/next Season main host, mandatory Home rules, or account-button fix exists. |
| Phase 4 — Stabilization/release preparation | **Missing as a product phase** | No Phase-4 migration tests, token backup exclusion, variant-safe notification package, Navori notification icon, borderless splash mark, Wear API-37 change, FOSS/GMS/Wear/lint/R8 matrix, English README/changelog, or release publishing gate exists. |
| Release `1.6.0.1` | **Missing** | `version.properties` is `1.6.0`/112; no `CHANGELOG.md`, tag, release asset, or final GitHub Release automation exists. |

## Visual comparison with the accepted screenshots

The backup cannot produce the supplied accepted UI without implementation work:

- no configurable navigation editor;
- no plus button or `Haupttab hinzufügen` sheet;
- no Calendar in the main bottom bar;
- no date-based Calendar week header and counts;
- no Settings and Account actions in the Home top bar;
- no dynamic Home/Discover shortcuts;
- no accepted five-tab configuration driven by preferences.

The current Home top bar contains only the notification action.

## Exact position in the roadmap

Use this label:

> **Recovery Phase / Phase 0: infrastructure baseline completed; Phase 1 has not started.**

This is earlier than the first accepted product phase.

## What must be rebuilt

### First

- recover Navori identity:
  - `app.navori`
  - `app.navori.debug`
  - launcher names
  - Navori README and assets
- restore `main` as product branch;
- keep `develop` as a verified upstream mirror;
- add AI context and recovery documentation.

### Then, sequentially

1. Phase 1 — configurable main navigation;
2. Phase 2 — date-based Calendar;
3. Phase 3 — modular Home/Discover/Season shortcuts and Home top-bar actions;
4. Phase 4 — migrations, accessibility, security, splash, variants, Wear, lint, R8, and release hardening;
5. manual device acceptance;
6. release `1.6.0.1`.

Do not skip directly to the version bump. The version number is not the missing value; the accepted product code is missing.

## Recommended next action

Create a new repository from this preserved baseline, commit or separately preserve the staged SDK-channel fix, establish `main` and `develop` correctly, and start the reconstructed Phase-1 prompt.

Before implementation, keep an immutable copy of this ZIP and the included `.git` directory.
