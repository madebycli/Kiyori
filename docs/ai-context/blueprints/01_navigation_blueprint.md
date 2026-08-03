> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Reconstructed Navigation Blueprint

> Compile-oriented design sketch, not exact deleted source.

## Model

```kotlin
enum class MainNavigationDestinationId(
    val stableId: String,
    val defaultVisible: Boolean,
) {
    HOME("home", true),
    ANIME("anime", true),
    MANGA("manga", true),
    PROFILE("profile", false),
    EXPLORE("explore", true),
    CALENDAR("calendar", false),
}

enum class SeasonShortcutMode { CURRENT, NEXT }

sealed interface MainNavigationShortcut {
    val stableId: String

    data class Season(val mode: SeasonShortcutMode) : MainNavigationShortcut {
        override val stableId = "shortcut_season_${mode.name.lowercase()}"
    }

    data class CurrentList(val type: CurrentListType) : MainNavigationShortcut {
        override val stableId = "shortcut_current_list_${type.name.lowercase()}"
    }

    data class Chart(val type: ChartType) : MainNavigationShortcut {
        override val stableId = "shortcut_chart_${type.name.lowercase()}"
    }
}

data class MainNavigationItem(
    val stableId: String,
    val visible: Boolean,
    val staticDestination: MainNavigationDestinationId? = null,
    val shortcut: MainNavigationShortcut? = null,
) {
    init {
        require((staticDestination == null) xor (shortcut == null))
    }
}
```

## Config normalization

```kotlin
const val MAIN_NAVIGATION_CONFIG_VERSION = 3
const val MIN_VISIBLE_MAIN_DESTINATIONS = 2
const val MAX_VISIBLE_MAIN_DESTINATIONS = 5

data class MainNavigationConfig(
    val version: Int,
    val items: List<MainNavigationItem>,
) {
    fun normalized(): MainNavigationConfig {
        val deduplicated = items
            .filter { it.staticDestination != null || it.shortcut != null }
            .distinctBy(MainNavigationItem::stableId)
            .toMutableList()

        ensureAllStaticDestinationsExist(deduplicated)
        forceHomeVisible(deduplicated)
        enforceSingleSeasonShortcut(deduplicated)
        enforceMaximumVisibleWhileProtectingHome(deduplicated)
        enforceMinimumVisible(deduplicated)

        return copy(
            version = MAIN_NAVIGATION_CONFIG_VERSION,
            items = deduplicated,
        )
    }
}
```

Preserve user order where possible. Never hide Home when reducing over-capacity.

## Default

```kotlin
fun defaultMainNavigationConfig(): MainNavigationConfig =
    MainNavigationConfig(
        version = MAIN_NAVIGATION_CONFIG_VERSION,
        items = listOf(
            static(HOME, visible = true),
            static(ANIME, visible = true),
            static(MANGA, visible = true),
            static(PROFILE, visible = false),
            static(EXPLORE, visible = true),
            static(CALENDAR, visible = false),
        )
    ).normalized()
```

## Codec

Support:

- unversioned legacy comma-separated visible static IDs;
- `v2;id:1,id:0`;
- `v3;stableId:1,...`.

```kotlin
fun decode(raw: String?): MainNavigationConfig = when {
    raw.isNullOrBlank() -> defaultMainNavigationConfig()
    raw.startsWith("v3;") -> decodeV3(raw.removePrefix("v3;"))
    raw.startsWith("v2;") -> migrateV2(raw.removePrefix("v2;"))
    else -> migrateLegacy(raw)
}.normalized()
```

Unknown shortcut parameters are discarded, not replaced randomly.

## Add shortcut

```kotlin
fun MainNavigationConfig.addShortcut(
    shortcut: MainNavigationShortcut,
): MainNavigationConfig {
    if (containsShortcut(shortcut)) return this
    if (shortcut is MainNavigationShortcut.Season && seasonShortcut != null) return this

    val initiallyVisible =
        visibleItems.size < MAX_VISIBLE_MAIN_DESTINATIONS

    return copy(
        items = items + MainNavigationItem(
            stableId = shortcut.stableId,
            visible = initiallyVisible,
            shortcut = shortcut,
        )
    ).normalized()
}
```

ViewModel emits Snackbar when added hidden because capacity is reached.

## Dynamic UI resolver

Create one resolver mapping item to:

- route;
- localized label;
- icon;
- selected predicate.

Bottom bar and rail both consume it. Do not duplicate switch logic in both components.
