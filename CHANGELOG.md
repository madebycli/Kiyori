# Changelog

## 1.6.0.1 — 2026-08-05

### Added

- A phone-only Kiyori release line with separate FOSS and GMS universal APKs.
- Main-navigation configuration version 4. New installations and untouched version-3 defaults use Home, Anime, Manga, Explore, and Calendar; Profile remains available but hidden by default.
- Lossless migration tests for custom tab order, visibility, five-tab layouts, and typed shortcuts.
- Responsive Discover action grids with two columns on compact phones and additional columns on wider layouts.
- Strong app locking with biometric or device-credential authentication, configurable timeout handling, process-lifecycle awareness, and protected deep-link delivery after unlock.
- Lock-safe notification routing for media, activity, thread, and user destinations.
- Focused character/team tests covering roles, voice languages, missing and multiple voice actors, and fallback behavior.

### Changed

- The accepted character/team presentation now has independent loading and empty states without changing its visual structure.
- Discover no longer duplicates Calendar as an action chip because Calendar is a first-class main destination.
- FOSS and GMS release metadata and Kiyori archive names are aligned.
- The audited upstream typography, rating-step, and nested thread-comment improvements are integrated.
- Release automation now builds only from canonical `main`, signs deterministic universal assets explicitly, verifies certificate and Android metadata, and emits `SHA256SUMS.txt`.

### Fixed

- Main-destination focus and fallback behavior when tabs are hidden, reordered, or replaced by shortcuts.
- App-lock relocking across rotation, internal navigation, authentication prompts, cold starts, and all configured timeout boundaries.
- Buffered widget, external-link, and notification destinations are consumed exactly once after successful unlock.
- Character and team loading states no longer block each other.
- Voice-language labels are deduplicated case-insensitively while preserving canonical display capitalization.

### Removed

- The Wear OS module, phone-to-Wear authentication bridge, Wear callback, Wear dependencies, version properties, CI inputs, and release artifacts.
