# AGENTS.md — Kiyori

## Required context

Before editing, read every Markdown file under `docs/ai-context/`.

## Branch rules

- `develop` is upstream-only and must remain an exact mirror.
- `recovery/phase0-backup` is immutable.
- `main` is the Kiyori product/default branch.
- Implement on feature branches and use pull requests.

## Product rules

- Product name is Kiyori; historical Navori references are recovery evidence only.
- Preserve AniList authentication, API endpoints and internal namespace unless explicitly approved.
- Reuse existing upstream screens and repositories instead of duplicating them.
- Keep Home mandatory and use one registry for bottom navigation and rail.
- Do not publish or version-bump before final owner acceptance.

## Safety

Never commit signing keys, passwords, tokens, `.secrets`, `local.properties`, private logs or APK signing material.

## Verification

Compile and test after each vertical slice. Before review, run the complete FOSS/GMS/Wear/test/lint/R8 matrix and produce an installable universal FOSS debug APK.
