# AI Context for Kiyori

This directory is the authoritative implementation handoff for rebuilding Kiyori from the preserved Phase-0 source backup.

## Read first

1. `../../NEXT_CHAT_START.md`
2. `CURRENT_STATE.md`
3. `PRODUCT_PLAN.md`
4. `DECISIONS.md`
5. `ARCHITECTURE_AND_SCOPES.md`
6. `UPSTREAM_BUILD_AUTH.md`
7. `KNOWN_FAILURES.md`
8. `PHASE_PROMPTS.md`
9. `reconstruction/11_INTEGRATED_REBUILD_COPY_PROMPT.md`
10. `blueprints/`

`SPEEDRUN_ALL_IN_ONE.md` combines the reconstructed plan and technical blueprints into one context file.

## Evidence

- `evidence/SOURCE_BACKUP_AUDIT.md` proves the source backup is Phase 0.
- `evidence/screenshots/` contains the accepted later UI reference.
- `evidence/APK_ARTIFACTS.md` lists surviving APKs and hashes; APK binaries are not committed.
- `historical/` is evidence only and may use the former project name.

## Authority rule

Current Kiyori files outside `historical/` take precedence over historical material. Reconstructed code snippets are not the exact deleted source and must be adapted to the current upstream tree.
