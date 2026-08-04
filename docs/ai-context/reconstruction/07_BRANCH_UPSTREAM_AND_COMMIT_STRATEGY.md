> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Branch, Upstream, and Commit Strategy

## New repository layout

Recommended exact names:

```text
develop
recovery/phase0-backup
main
feature/kiyori-integrated-rebuild
```

## Import rules

### `recovery/phase0-backup`

Push the backup exactly and preserve its Git history. Decide separately whether to commit the staged SDK-channel fix. Never rewrite this branch.

### `develop`

Fetch current:

```text
axiel7/AniHyou-android:develop
```

Verify SHA and make fork `develop` an exact mirror.

### `main`

Create from current `develop`, not the stale Phase-0 backup. Apply Kiyori identity and product work through pull requests.

## Porting Phase-0 infrastructure

Compare:

- workflows;
- Crowdin;
- `.gitignore`;
- `docs/BUILDING.md`;
- `shell.nix`.

Current upstream may have newer actions, SDK requirements, or Gradle tasks. Reapply intent rather than blindly cherry-picking whole files.

## Integrated branch

Create:

```text
feature/kiyori-integrated-rebuild
```

All feature work may happen here, but commits must remain coherent and CI must stay recoverable.

## PR strategy

One large product PR is acceptable for the speedrun only if:

- commits are reviewable;
- no unrelated upstream changes are mixed in;
- full CI is green;
- PR body contains requirement checklist;
- APK artifact is attached;
- owner approves final device test.

## Upstream movement during rebuild

Freeze upstream base SHA in PR description. Do not merge new upstream halfway through unless required for a blocker. After acceptance, perform a separate upstream sync.

## Secrets

Never commit:

- keystore;
- passwords;
- Base64 keystore;
- OAuth tokens;
- local properties;
- APK signing secrets;
- private device logs.

## AI handoff files

Create:

```text
AGENTS.md
docs/ai-context/README.md
docs/ai-context/CURRENT_STATE.md
docs/ai-context/PRODUCT_PLAN.md
docs/ai-context/DECISIONS.md
docs/ai-context/ARCHITECTURE_AND_SCOPES.md
docs/ai-context/UPSTREAM_BUILD_AUTH.md
docs/ai-context/PHASE_PROMPTS.md
```

Adapt this package. Do not copy historical “merged” status before the new rebuild reaches it.
