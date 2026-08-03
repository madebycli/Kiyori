# Start Here in the Next Chat

You are working on **Kiyori** in `madebycli/Kiyori`.

## What this ZIP contains

- a complete Git repository including `.git`;
- checked-out branch `main`;
- immutable branch `recovery/phase0-backup`;
- the full Phase-0 backup history;
- Kiyori AI context, plans, prompts, blueprints and visual evidence;
- no keystore, signing password, API token, `local.properties`, private log or APK binary.

The remote repository already has a newer `develop` branch. It is not embedded in this offline ZIP because the environment could not fetch its Git objects. The recorded remote develop SHA at packaging time was:

```text
01a8a4abe98c778d1015a33072a11efdb4ef8593
```

Reverify it because upstream may have advanced.

## First task for the new AI

1. Inspect `git status`, `git branch -avv`, `git log --graph --all`.
2. Read `AGENTS.md` and all Markdown under `docs/ai-context/`.
3. Connect to `https://github.com/madebycli/Kiyori`.
4. Fetch `develop` and verify it is the upstream-only branch.
5. Push `main` and `recovery/phase0-backup` without force.
6. Set GitHub default branch to `main`.
7. Create `feature/kiyori-integrated-rebuild` from current `origin/develop`.
8. While on that feature branch, copy only context files from `main`:

```bash
git checkout main -- AGENTS.md KIYORI_RECOVERY.md NEXT_CHAT_START.md docs/ai-context
git commit -m "docs: add Kiyori reconstruction context"
```

9. Port useful Phase-0 build infrastructure by intent, not by blindly merging the stale backup tree.
10. Execute the integrated rebuild prompt.

## Safe upload commands

```bash
git remote set-url origin https://github.com/madebycli/Kiyori.git
git fetch origin develop master
git push origin main:main
git push origin recovery/phase0-backup:recovery/phase0-backup
gh repo edit madebycli/Kiyori --default-branch main
```

Never force-push over a branch that unexpectedly already exists. Inspect first.

## Product naming

- Visible product: Kiyori
- Target release ID: `app.kiyori`
- Target debug ID: `app.kiyori.debug`
- Internal Kotlin namespace remains `com.axiel7.anihyou`
- Historical references to the former project name are evidence only.
