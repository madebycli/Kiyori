# AGENTS.md — Kiyori

## Required context

Before editing, read `NEXT_CHAT_START.md` and every Markdown file under `docs/ai-context/`.

## Branch rules

- `develop` is upstream-only and must remain an exact mirror of the selected `axiel7/AniHyou-android:develop`.
- `recovery/phase0-backup` is immutable.
- `main` is the Kiyori product/default branch.
- Build Kiyori on `feature/kiyori-integrated-rebuild` created from current `develop`.
- Use reviewed pull requests. Never force-push over preserved branches.

## Baseline truth

The preserved source is Phase 0 / pre-Phase-1 infrastructure. It does not contain the accepted configurable navigation, date Calendar, shortcut registry or Phase-4 product hardening.

## Product rules

- Product name is Kiyori; historical references to the former name are recovery evidence only.
- Target IDs are `app.kiyori` and `app.kiyori.debug`.
- Preserve AniList authentication, API endpoints, OAuth callback contract and internal Kotlin namespace unless explicitly approved.
- Reuse upstream screens, ViewModels, queries and repositories instead of duplicating them.
- Home is mandatory startup and fallback.
- Bottom navigation and rail use one persisted typed registry.
- Do not publish or version-bump before final owner acceptance.

## Safety

Never commit signing keys, passwords, tokens, `.secrets`, `local.properties`, private logs, APK binaries or signing material.

## Verification

Compile and test after each vertical slice. Before review, run the complete FOSS/GMS/Wear/test/lint/R8 matrix and produce an installable universal FOSS debug APK.

## Honesty

Reconstructed Kotlin snippets are implementation blueprints, not byte-identical deleted source. Adapt them to current upstream APIs and prove behavior with tests.
