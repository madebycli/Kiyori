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
