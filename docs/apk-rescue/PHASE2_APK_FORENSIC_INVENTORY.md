# Phase 2 APK forensic inventory

Source artifact: `Navori-Plan2-Phase2-foss-universal-debug.apk`

SHA-256: `0ce02d44af375f1b934a47d73f807d0b271717990ce088652785a29db7c8a6bb`

## Visual source of truth

The following screenshots represent the **good lost implementation** and must be treated as acceptance references:

- weekly Calendar main tab with a date range, previous/next week controls, weekday/date/count strip, selected underline, list/grid switch and filter;
- `Hauptnavigation anpassen` editor with destination icons, switches, drag handles, two-to-five visible destination rule and a required Home destination;
- `Haupttab hinzufügen` bottom sheet grouped by Home and Explore shortcut categories;
- Home top actions for notifications, settings and account/profile;
- media-details Characters/Team view pairing character and voice-actor portraits with role/language filters.

The simple weekday-tab Calendar and the editor using arrow buttons are the rejected reconstruction and are not acceptance references.

## APK condition

- Android debug APK with 12 DEX files.
- Application classes and method names are not generally obfuscated.
- Android Gradle Plugin metadata reports AGP 9.3.0.
- The APK is used only to recover owned application behavior and UI structure. Signing material and credentials are explicitly out of scope.

## Recovered main-navigation architecture

The APK contains these source-level structures:

- `MainNavigationConfig.kt`
- `MainNavigationShortcut.kt`
- `MainNavigationEntry` with `Static` and `Shortcut` implementations
- `MainNavigationDestinationId`
- `MainNavigationIconKey`
- `MainNavigationShortcutType`
- `MainNavigationShortcutCategory`
- `MainNavigationShortcutDefinition`
- `MainNavigationShortcutRegistry`
- `MainNavigationPreferencesRepository`
- `MainNavigationSettingsUiState`
- `MainNavigationSettingsEvent`
- `MainNavigationSettingsView.kt`

Confirmed behavior and signatures:

- `MainNavigationConfig.move(fromIndex, toIndex)`
- `canSetVisibility(entry, visible)`
- `setVisibility(entry, visible)`
- `addShortcut(shortcut)`
- `removeShortcut(shortcut)`
- `containsShortcut(shortcut/type)`
- `resolveVisibleDestination(destination)`
- `visibleItems`, `visibleDestinations`, `shortcuts` and a single season shortcut
- shortcut registry categories for current lists, seasons and charts
- Compose editor backed by `ReorderableLazyListState`
- separate `MainNavigationShortcutPicker`
- editor row function `MainNavigationEditorItem`

## Recovered Calendar architecture

The APK contains:

- `CalendarDateWindow`
- `CalendarDateRange`
- `CalendarDayBounds`
- `CalendarHostViewModel`
- `CalendarListFilter`
- `CalendarUiState`
- `CalendarView.kt`

Confirmed UI functions:

- `CalendarView(..., isMainDestination, modifier)`
- `CalendarViewContent(...)`
- `CalendarDateStrip(...)`
- `CalendarDateTab(...)`
- `CalendarDayView(...)`
- `CalendarFilterMenu(...)`
- `CalendarList(...)`
- `CalendarGrid(...)`

The main destination uses date-based navigation rather than the rejected weekday-only tab row.

## Recovered media-details character/staff architecture

The APK contains:

- `MediaCharactersAndStaff`
- `MediaCharactersAndStaffQuery`
- `MediaDetailsViewModel.fetchCharactersAndStaff`
- character edges containing character role and voice-actor data
- staff edges and dedicated staff data

The screenshots establish the intended UI: Characters/Team segmented control, character-role filters, language selector, paired character/voice-actor rows and portraits.

## Recovered App Lock architecture

The current source tree does not contain these files, but the APK does:

- `AppLockPreferences.kt`
- `AppLockPreferencesRepository`
- `AppLockSettings.kt`
- `AppLockAuthenticationChange`
- `SystemAuthenticationPrompt`
- `SystemAuthenticationAvailability`
- `SystemAuthenticationError`
- `AppLockRuntime.kt`
- `AppLockScreen.kt`

Confirmed settings model:

- `AppLockPreferences(enabled, timeout)`
- timeout values: `IMMEDIATELY`, `ONE_MINUTE`, `FIVE_MINUTES`, `FIFTEEN_MINUTES`, `THIRTY_MINUTES`
- enabling/disabling requires successful system authentication
- runtime tracks initialization, enabled state, timeout, locked state and monotonic background timestamp
- foreground transition locks after the configured elapsed timeout
- authentication success clears the locked state

## Recovery order

1. Restore the high-quality main-navigation model/editor and correct Calendar main-tab projection.
2. Restore Calendar date-strip/list/grid/filter behavior and visual hierarchy.
3. Restore Characters/Team media-details presentation.
4. Restore App Lock settings, biometric/device-credential prompt and runtime lifecycle gate.
5. Add regression tests and build FOSS/GMS debug artifacts before any merge into `main`.
