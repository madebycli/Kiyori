# Known Failures and Preventive Rules

These failures occurred during the previous implementation and must be prevented.

1. **Calendar GraphQL field mismatch** — `genres` was not selected by the historical `AiringAnimesQuery.Media`; inspect generated query types before using fields. `meanScore` was available historically.
2. **Dead Home account button** — navigating to a hidden Profile main tab triggered immediate fallback to Home. Navigate to the nested own-profile route.
3. **Picker compile error** — missing Compose Foundation import. Compile the settings feature immediately after adding the scrollable bottom sheet.
4. **Unsupported `matchParentSize()`** — adapt to the current Compose version; use explicit constrained sizing where needed.
5. **Wear compile SDK mismatch** — dependencies required API 37 while Wear was on 36. Raise compile SDK only when required; keep target SDK changes explicit.
6. **Phone shortcut leaked into Wear** — phone launcher shortcut XML must live in the phone app module.
7. **Disabled Gradle `resValue`** — use Android resource overlays for release/debug package strings.
8. **Hardcoded release notification package** — use `applicationContext.packageName` or equivalent installed-variant resolution.
9. **Accidental whole-file replacement** — fetch current blobs, apply minimal edits and inspect full diffs for Gradle/version catalog files.
10. **Ambiguous dynamic icons** — distinguish dynamic Home/chart destinations with decorative overlays that are ignored by TalkBack.
11. **Stale Season rollover** — persist CURRENT/NEXT only; resolve season/year at runtime and include them in ViewModel identity.
12. **Calendar count/content divergence** — one host-owned filter and one date-bound function must drive both.
13. **Home hidden by malformed migration** — normalize and protect Home before enforcing capacity.
14. **Remove/X button too low** — use one shared row height and centered fixed 48dp action slots; test larger font scales.
15. **Pager state desynchronization** — selected page is canonical, date is derived, page range is clamped 0..14, and week header derives from selected date.
16. **Shared-resource branding leak** — launcher/splash resources inherited from the common module also
   affect Wear. Keep Kiyori phone identity in app resource overlays unless a Wear-specific change is explicitly scoped.
17. **Variant-specific notification intent** — never use a compile-time package constant for notification launch intents; use the installed application context package.
18. **Navigation config repair drift** — always pass decoded, migrated and edited configuration
    through one normalizer; Home must remain visible and malformed data must not select a hidden tab.
19. **Silent legacy persistence** — decode-only migration leaves corrupt values on disk. Invoke the
    explicit normalized rewrite during app initialization before the editor can modify the config.
20. **Divergent compact/wide navigation** — never maintain separate bottom-bar and rail lists; both
    must consume the resolver projection of the same persisted typed configuration.
21. **Connector blob encoding** — upload text sources as UTF-8 and verify a fetched source file; truncated base64 can create binary blobs.
22. **JVM-only repository DNS failure** — Gradle can fail before source configuration although curl
    reaches the same repository. Validate the wrapper separately, then use a local cached mirror
    rather than changing production dependency coordinates.
23. **Dynamic shortcut treated as nested navigation** — construct `NavigationState` from the
    resolved typed route set, not a static list of destinations.
24. **Calendar header count mismatch** — do not derive a count from an unfiltered first page. Use the
    same date bounds, filters and pagination as the selected Calendar page; show unavailable rather
    than zero after a query error.
25. **Ephemeral localhost dependency proxy** — a proxy started inside a short-lived Work command can
    be terminated before Gradle finishes. Do not change production repositories to compensate; use CI
    or a persistent proxy for the validation matrix.
26. **Premature release metadata** — release-candidate documentation is not approval to bump a version,
    create a tag, or publish an artifact. Keep metadata stable until signed CI and device acceptance finish.
27. **Independent main README conflict** — `main` has a post-branch README append while the Kiyori
    feature replaces the inherited README. Do not silently merge/rebase under the campaign rules;
    obtain an explicit maintainer choice for resolving that one file.
28. **Partial offline Gradle cache** — Gradle may start but still lack the included-build Kotlin DSL
    marker/plugin. Treat the exact missing coordinate as dependency hydration work, never as evidence
    of a Kiyori Kotlin compile failure.
29. **Android test annotation mismatch** — `kotlin.test.Test` is not present on this Android unit-test
    classpath even with `kotlin("test")`. Use the existing JUnit 4 dependency's `org.junit.Test` annotation.
30. **Shortcut data-class constructor** — a data class cannot forward non-property primary constructor
    parameters to `BottomDestination`; use a regular class when only the stable shortcut ID is state.
31. **Calendar lazy placeholder overload** — the aliased list `items` import does not provide the count overload.
    Use `repeat { item { ... } }` for LazyColumn placeholders and keep the grid overload separate.
32. **Main-host navigation lambda typing** — `navigationIcon` is a composable slot, not a nullable
    lambda value. Keep the slot non-null and render the Back action conditionally inside it.
33. **Settings top-app-bar API drift** — Material 3's `TopAppBar` needs the current experimental opt-in
    and a real `TopAppBarScrollBehavior`; do not pass a bare remembered state as `scrollBehavior`.
34. **Release-only reorderable artifact** — the debug and release coordinates are distinct. Never copy
    `reorderable-android-debug` into a release cache or change production dependencies merely to make
    an offline release task configure.
35. **Work network approval cancellation** — an error stating that network approval was cancelled before
    a decision was returned is an external sandbox condition. Preserve the exact missing coordinate and
    retry only when Maven access is available; do not diagnose it as a Gradle or source defect.
36. **Kotlin temporary-directory fallback** — set `TMPDIR`, `java.io.tmpdir`, and Kotlin daemon JVM args
    to a writable workspace directory before compiling. An unusable `/tmp` can make daemon fallback
    surface misleading duplicate-declaration diagnostics.
