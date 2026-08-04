> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Kiyori Speedrun Recovery v2

## Mission

Rebuild the accepted Kiyori product from the surviving **pre-Phase-1 source backup**, while using the previous implementation experience to avoid repeating discovery work and known mistakes.

This package is written for a coding AI. It contains:

- the exact recovered baseline classification;
- an integrated implementation strategy;
- historical behavior and class/path knowledge;
- reconstructed Kotlin-oriented blueprints;
- known failures and their fixes;
- visual acceptance requirements;
- a consolidated final device test plan;
- optional APK forensics;
- release reconstruction.

## Critical truth

The surviving source ZIP is:

```text
Recovery Phase / Phase 0
branch: recovery/phase0-backup
HEAD: 476ad447217ecae2b7c7ae710f7981ca55d9a003
base upstream: 259e81de6cd3ea51a488849bbd4777a2c3c7f342
version: 1.6.0 / code 112
```

It contains build, NixOS, CI, Crowdin, and signing preparation only. It does not contain the accepted configurable navigation, date Calendar, shortcut registry, Phase-4 hardening, or final Kiyori branding.

## Speedrun strategy

Do not repeat four manual owner-test cycles.

Use:

- one integrated implementation branch;
- small internal commits grouped by vertical slice;
- automated compile/test gates after each slice;
- one installable FOSS debug APK for owner testing at the end;
- one correction pass if required;
- then release preparation.

This is not permission for one giant untested commit. The coding AI must keep the branch buildable internally.

## Evidence labels

- **Historical fact** — recorded from the prior implementation and CI/manual acceptance.
- **Screenshot fact** — visible in the supplied owner screenshots.
- **Reconstructed blueprint** — inferred from recorded classes, routes, tests, and fixes; not byte-identical lost source.
- **New v2 improvement** — intentionally better than the old accepted build, such as swipe-between-days in Calendar.

## Required reading order

1. `01_AI_BOOTSTRAP_SPEEDRUN_PROMPT.md`
2. `02_SPEEDRUN_MASTER_PLAN.md`
3. `03_RECOVERED_IMPLEMENTATION_MAP.md`
4. `04_KNOWN_FAILURES_AND_PREVENTION.md`
5. `05_UI_UX_CONTRACT_AND_IMPROVEMENTS.md`
6. `06_TEST_ONCE_FINAL_MATRIX.md`
7. `07_BRANCH_UPSTREAM_AND_COMMIT_STRATEGY.md`
8. `08_APK_FORENSICS_GUIDE.md`
9. `09_RELEASE_RECONSTRUCTION.md`
10. files under `blueprints/`

`SPEEDRUN_ALL_IN_ONE.md` contains the full textual context in one file.

## Non-negotiable honesty

Never claim reconstructed snippets are the exact deleted source. They are implementation-ready guidance that must be adapted to the current upstream tree and compiled.
