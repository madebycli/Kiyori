# Kiyori release notes

## 1.6.0.1 phone-only release candidate

Kiyori 1.6.0.1 restores and consolidates the complete recovered phone product while retaining AniList
sign-in, package IDs, internal namespace, and API compatibility.

- The default main navigation is now Home, Anime, Manga, Explore, and Calendar. Existing custom order,
  visibility, five-tab layouts, Profile choices, and typed shortcuts migrate without destructive reset.
- Discover keeps its established action-chip design, removes the duplicate Calendar action, and adapts
  from two columns on compact phones to additional columns on wider layouts.
- Character and team content keeps the accepted presentation while adding independent loading and empty
  states, robust long-text handling, role filters, language selection, and voice-actor fallback tests.
- App lock uses strong biometric or device credentials, process-level foreground/background timing, all
  configured timeout boundaries, no protected-content flash, and exact-once destination delivery after unlock.
- Notifications continue to be generated and displayed while locked. Media, activity, thread, and user
  taps pass through the same lock-safe deep-link gate as widgets and external links.
- The audited upstream typography, rating-step, and nested thread-comment changes are integrated.
- Wear OS, its authentication bridge, build inputs, metadata, dependencies, and release artifacts are removed.
- Release automation produces only signed FOSS/GMS universal APKs plus `SHA256SUMS.txt` from canonical `main`.

## Validation and publication gate

Android CI run 140 passed the full pre-version FOSS/GMS debug, unit/module test, lint, minified release,
and R8 matrix on commit `1811092adfe6fe28efe536eda26b092e4797d4b0`. Its uploaded artifact
`android-ci-140` has GitHub digest
`sha256:339edd1603320dbdcf534a01ac49acc06042b0282a374eaa88fc4e84ff7dbbb7`.

The versioned `1.6.0.1` / code 114 candidate must pass the same matrix. Publication additionally requires
real-device/upgrade acceptance and verified signing identity. Until tag, three exact assets, checksums,
and release state are verified on GitHub, this document describes a release candidate rather than a
published release.
