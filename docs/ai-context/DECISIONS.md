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
- Kiyori launcher and splash assets live in the phone app module; the upstream shared resource module is
  intentionally not repurposed, keeping Wear identity outside this phone-first rebuild scope.
- Notification code resolves a phone-provided Kiyori monochrome resource at runtime and falls back to the upstream mark for non-phone consumers.
- Main navigation persistence uses stable item IDs plus semantic shortcut parameters only. It never
  serializes a Compose route, a concrete season/year, or a ViewModel instance key.
- The navigation schema shares the existing default-preferences DataStore; normalization is explicit
  and idempotent, so migrating legacy values cannot disturb credential storage.
- One app-local resolver projects the typed configuration into compact and wide navigation. It sends
  any hidden or removed active top-level target to Home instead of retaining an unreachable route.
- The top-level Calendar route is distinct from the retained nested route so only the nested view has a Back action.
- The current default DataStore carries credentials, so its file is excluded from cloud backup and device transfer.
- Calendar's list/grid choice is a separate Boolean preference; it defaults to list and never changes
  the date range or tri-state filter semantics.
- Dynamic current-list, chart and season routes carry a serializable `isMainDestination` flag so a
  reused host can remove only its main-tab Back affordance without altering nested navigation.
- The app creates navigation back stacks from the resolved registry, not a static destination set;
  adding or removing a typed shortcut therefore remains a real top-level navigation change.
- Dynamic shortcut icons are category-specific, while the localized destination label remains the
  sole TalkBack description supplied by the standard navigation item.
- Calendar week-header counts are exact filtered counts. They use the same timezone-safe daily bounds,
  adult setting and tri-state list filter as the associated pager page; failed count requests display
  an unavailable marker rather than a misleading zero.
- Existing version metadata remains unchanged until CI, signed artifacts and device acceptance pass;
  release-candidate documentation must not be interpreted as a published release.
- The independent `main` README change is not incorporated automatically: this campaign prohibits
  merge and rebase. A maintainer must explicitly choose the permitted conflict-resolution workflow.
- Android-local unit tests use the established JUnit 4 annotation from `libs.junit`; assertion helpers may remain
  from `kotlin.test`, but the platform test annotation must resolve on the Android unit-test classpath.
- Validation may use a workspace-local Java 17, Gradle cache, SDK and writable temporary directory; none
  of those environment aids are committed as product dependencies or source changes.
- A release artifact must resolve its declared release coordinates. Debug-only artifacts are never copied
  or substituted to produce an apparently successful offline release validation.
