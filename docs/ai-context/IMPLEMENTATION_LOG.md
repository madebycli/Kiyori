# Kiyori Implementation Log

## 2026-08-04 — Preflight bootstrap

- Read the checkpointed master prompt, its checkpoint policy, the required project context, and the reconstruction material needed to begin safely.
- Verified `feature/kiyori-integrated-rebuild` is clean at `621c2831`; its merge-base with `origin/develop` is `01a8a4ab`.
- Verified the protected refs remain unchanged: `origin/main` is `90898bfe`, `origin/develop` is `01a8a4ab`, and `origin/recovery/phase0-backup` is `476ad447`.
- Verified no tracked or worktree keystores, private keys, `local.properties`, `.env`, or `.secrets` files.
- Verified the existing AniList client ID, callback, GraphQL endpoint, token handling references, and Kotlin namespace remain untouched.
- GitHub has no PR for this feature head. `main` contains one README-only commit after the feature baseline, so GitHub requires this documentation checkpoint before it can open a draft PR.

## 2026-08-04 — Publication recovery

- Local Git lacks a GitHub credential helper and the GitHub CLI is unavailable in this Work workspace.
- The preflight checkpoint is therefore being published through the repository's connected GitHub integration; no protected branch is being altered.

## 2026-08-04 — Gate 1, application identity

- Published the application-identity checkpoint through the connected GitHub integration and synchronized the local feature checkout to `af5bef6b`.
- Changed only the phone application ID and visible names: release `app.kiyori`, debug `app.kiyori.debug`, `Kiyori`, and `Kiyori Debug`.
- Preserved the internal `com.axiel7.anihyou` namespace, OAuth callback, AniList client ID, GraphQL endpoints, and token handling.
- `git diff --check` passed. The FOSS debug assembly is blocked before configuration because Gradle 9.5.0 is not cached and this Work environment cannot reach `services.gradle.org`; Java 17 itself works with the JDK library directory on `LD_LIBRARY_PATH`.

## 2026-08-04 — Gate 1, phone launcher and splash assets

- Added an original abstract Kiyori K mark as editable SVG plus a 512×512 PNG preview and short design/originality note.
- Added phone-scoped adaptive foreground/background, legacy vector fallback, API-26 themed monochrome layer, and a dedicated transparent splash vector; no common/Wear resource changed.
- The app manifest now selects the Kiyori launcher resource and `Theme.KiyoriSplash`, which uses the dark navy splash background and transparent mark.
- A remote tree-construction mistake was detected and repaired immediately with a forward-only commit; the verified remote head again contains the complete repository tree and the intended four source/preview files.

## 2026-08-04 — Gate 1, notification and protected-contract closeout

- Added an all-white Kiyori notification mark in the phone app module. The shared notification utility uses it only when supplied by the installed package and otherwise retains the upstream fallback.
- Notification launch intents now resolve `applicationContext.packageName`, preventing a release-package constant from breaking the debug variant.
- Post-branding protected-contract comparison found no semantic change to the AniList client ID, callback, GraphQL endpoint, or token handling.

## 2026-08-04 — Gate 2, typed navigation model and codec

- Added a version-three, route-independent typed navigation contract in `core:model`.
- The codec recognizes blank/default, legacy static-only, v2 and v3 values, discards unknown values,
  preserves user order where possible, and rewrites all accepted values as v3.
- Normalization protects Home, restores all static destinations, limits visibility to two through five,
  prevents duplicate items and permits only one Season shortcut.

## 2026-08-04 — Gate 2, preferences persistence

- Added a shared `main_navigation_config` preferences flow, a normalized writer, and an idempotent
  migration method to the existing default preferences repository.
- The schema stays separate from credentials and stores only the compact v3 technical-item contract.

## 2026-08-04 — Gate 2, shared app shell

- Added one app-local resolver for the normalized typed configuration and connected it to the phone
  bottom bar and wide navigation rail.
- Home is now the deterministic startup and fallback; an active top-level route removed or hidden by
  the persisted configuration immediately returns to Home.
- Calendar is represented as a configurable typed top-level target without changing the nested route.

## 2026-08-04 — Navigation, Calendar, and security continuation

- Added an editable main-navigation settings route with visibility, ordering, reset, remove and typed shortcut controls.
- Projected current-list, chart and semantic current/next-season shortcuts through the shared compact/wide resolver.
- Added a date-based Calendar main host with a bounded local-date pager, Monday-first week controls and DST-safe interval tests.
- Added Home Account and Settings actions and excluded the credential-bearing DataStore from backup and device transfer.
