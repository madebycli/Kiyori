# Current State — Kiyori

## Repository

- Repository: `madebycli/Kiyori`
- Product name: **Kiyori**
- Product branch: `main`
- Upstream mirror: `develop`
- Preserved immutable backup ref: `recovery/phase0-backup`
- Integrated rebuild branch to create next: `feature/kiyori-integrated-rebuild`

## Verified refs at context bootstrap

- Preserved Phase-0 backup HEAD: `476ad447217ecae2b7c7ae710f7981ca55d9a003`
- Historical upstream base contained by backup: `259e81de6cd3ea51a488849bbd4777a2c3c7f342`
- Newer repository `develop` HEAD: `01a8a4abe98c778d1015a33072a11efdb4ef8593`
- Backup version metadata: `1.6.0`, Android code `112`

## Exact phase classification

The preserved source is **Phase 0 / pre-Phase-1 infrastructure baseline**.

Present:

- NixOS build shell;
- Android CI and manual signing workflow foundations;
- Crowdin workflow hardening;
- signing file ignore rules;
- build/signing documentation;
- one staged SDK channel fix, committed during Kiyori bootstrap.

Missing from source and therefore to rebuild:

1. configurable primary navigation;
2. date-based Calendar main tab;
3. modular Home/Discover/Season shortcuts;
4. Phase-4 migration, accessibility, security, branding, Wear and CI hardening;
5. final Kiyori branding and first public release.

## Important naming rule

The previous product name in the lost implementation was Navori. It is retained only in historical recovery notes. All new product code, documentation, branches, release titles and visible branding must use **Kiyori**.

The internal Kotlin namespace `com.axiel7.anihyou` should remain unchanged unless an explicit architecture decision says otherwise.

## Next implementation action

Create `feature/kiyori-integrated-rebuild` from the current `develop` branch. Port compatible Phase-0 infrastructure intentionally, then implement the full integrated rebuild described in `PRODUCT_PLAN.md` and `PHASE_PROMPTS.md`. The owner will perform one consolidated device test after the complete FOSS debug build.
