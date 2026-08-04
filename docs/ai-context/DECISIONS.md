# Durable Decisions — Kiyori

- Product name is Kiyori.
- `main` is the default product branch.
- `develop` remains an exact upstream mirror.
- The imported backup remains immutable under `recovery/phase0-backup`.
- Rebuild from current `develop`, not directly on the stale backup tree.
- Use one integrated implementation branch and one consolidated owner device test.
- Home is mandatory, startup and fallback.
- Visible main destinations: minimum 2, maximum 5.
- Bottom navigation and navigation rail use one persisted registry.
- Calendar range is today through today+14 inclusive and uses Monday–Sunday weeks.
- Calendar supports list/grid, compact tri-state filter and horizontal day swipe.
- Current/next Season stores semantic mode only, not concrete season/year.
- Home account opens nested own profile.
- Existing Home list, chart and Season data layers must be reused.
- Dynamic remove/X actions must be vertically centered in fixed 48dp slots.
- Public README, changelog and release notes are English.
- No signing key, password or token enters Git history.
