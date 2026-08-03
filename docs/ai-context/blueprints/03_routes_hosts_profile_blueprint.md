> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Routes, Reused Hosts, and Profile Blueprint

## Routes

Use current upstream typed/serializable route conventions.

Conceptually:

```kotlin
sealed interface Routes {
    data object Calendar : Routes
    data object CalendarMain : Routes

    data class SeasonAnime(
        val season: MediaSeason,
        val year: Int,
    ) : Routes

    data class SeasonMain(
        val mode: SeasonShortcutMode,
    ) : Routes

    data class ChartMain(
        val chartType: ChartType,
    ) : Routes

    data class CurrentListMain(
        val listType: CurrentListType,
    ) : Routes
}
```

## Host parameter pattern

Do not clone views.

```kotlin
@Composable
fun SeasonAnimeView(
    season: MediaSeason,
    year: Int,
    isMainDestination: Boolean = false,
    onBack: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
) {
    ExistingSeasonScaffold(
        showBack = !isMainDestination,
        contentPadding = contentPadding,
    )
}
```

Original nested call uses defaults. Main call sets `isMainDestination = true`.

Use same approach for charts and current full lists.

## Home account

Bad:

```kotlin
onAccountClick = { navigateToMainDestination(PROFILE) }
```

Correct concept:

```kotlin
onAccountClick = navActionManager::toOwnProfile
```

Nested route is not filtered by visible main destinations.

## Top-level selection

Map current route to exact `MainNavigationItem.stableId`.

Parameterized routes include parameter identity so two chart shortcuts coexist.

## Hidden active destination

```kotlin
if (activeMainItemStableId !in visibleStableIds) {
    navigateToHome(clearTopLevelSelection = true)
}
```

Do not apply to nested profile/details routes.

## Startup

Always establish Home as initial top-level destination. Do not restore Calendar/Season/chart as automatic startup.
