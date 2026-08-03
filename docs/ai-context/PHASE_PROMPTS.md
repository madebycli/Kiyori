# Phase Prompts — Kiyori Integrated Rebuild

## Prompt 0 — Verify repository and create integrated branch

Read `AGENTS.md` and every file in `docs/ai-context/`. Verify `main`, `develop`, `recovery/phase0-backup` and current upstream. Confirm no secrets are tracked. Create `feature/kiyori-integrated-rebuild` from `develop`. Record all SHAs in `CURRENT_STATE.md`.

## Prompt 1 — Foundation and identity

Port only compatible Phase-0 infrastructure from `main`/backup. Implement Kiyori identity (`app.kiyori`, `app.kiyori.debug`, visible names and assets) while preserving upstream auth/API contracts and internal namespace. Add dedicated transparent splash asset. Compile FOSS debug.

## Prompt 2 — Typed navigation platform

Implement static and typed dynamic navigation items, versioned persistence, Home protection, profile migration, bottom/rail parity, settings editor, plus picker, capacity feedback, exact removal and centered X actions. Add model/domain/route tests and build FOSS debug.

## Prompt 3 — Calendar vertical slice

Implement the date-based Calendar main/nested split, today..today+14, Monday week, counts, list/grid persistence, compact tri-state filter, timezone/DST handling and swipe-between-days with automatic week transition. Reuse current upstream media items and generated query fields only. Add tests and build FOSS debug.

## Prompt 4 — Home, charts and Season shortcuts

Add Home notification/settings/account actions, fixing account navigation to nested own profile. Expose all five Home lists, all accepted Anime/Manga charts and current/next Season through the typed registry. Reuse existing views/ViewModels and retain original nested pages. Add route/registry tests and build FOSS debug.

## Prompt 5 — Stabilization and final test artifact

Harden migrations, accessibility, compact/wide/OLED, notification routing, token backup exclusion, phone/Wear resources and full CI. Build FOSS/GMS debug, Wear debug, tests, lint and minified unsigned release candidate. Produce one installable universal FOSS debug APK for owner acceptance. Do not publish a release yet.

## Prompt 6 — Release after owner acceptance

After explicit owner approval, choose and document Kiyori release metadata, keep README/changelog/release notes English, configure permanent signing secrets, build from `main`, verify with `apksigner`, tag and publish the signed universal FOSS APK. Never commit or print keys.
