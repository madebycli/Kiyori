package com.axiel7.anihyou.core.model.navigation

import com.axiel7.anihyou.core.model.CurrentListType
import com.axiel7.anihyou.core.model.media.ChartType

/**
 * APK-recovered presentation contract for entries shown in the configurable
 * phone navigation. Persisted values continue to use the stable ids from
 * [MainNavigationConfig]; this layer supplies the richer editor semantics.
 */
typealias MainNavigationDestinationId = MainNavigationDestination

enum class MainNavigationIconKey {
    HOME,
    ANIME,
    MANGA,
    PROFILE,
    EXPLORE,
    CALENDAR,
    AIRING,
    BEHIND,
    WATCHING,
    READING,
    SEASON,
    NEXT_SEASON,
    TOP,
    POPULAR,
    UPCOMING,
    MOVIES,
    PUBLISHING,
}

enum class MainNavigationShortcutCategory {
    HOME,
    DISCOVER,
}

enum class MainNavigationShortcutType {
    CURRENT_LIST,
    SEASON,
    CHART,
}

sealed interface MainNavigationEntry {
    val stableId: String
    val iconKey: MainNavigationIconKey

    data class Static(
        val destination: MainNavigationDestinationId,
    ) : MainNavigationEntry {
        override val stableId: String = destination.stableId
        override val iconKey: MainNavigationIconKey = destination.iconKey
    }

    data class Shortcut(
        val shortcut: MainNavigationShortcut,
    ) : MainNavigationEntry {
        override val stableId: String = shortcut.stableId
        override val iconKey: MainNavigationIconKey = shortcut.iconKey
    }
}

data class MainNavigationShortcutDefinition(
    val type: MainNavigationShortcutType,
    val category: MainNavigationShortcutCategory,
    val shortcuts: List<MainNavigationShortcut>,
)

object MainNavigationShortcutRegistry {
    val definitions: List<MainNavigationShortcutDefinition> = listOf(
        MainNavigationShortcutDefinition(
            type = MainNavigationShortcutType.CURRENT_LIST,
            category = MainNavigationShortcutCategory.HOME,
            shortcuts = CurrentListType.entries.map(MainNavigationShortcut::CurrentList),
        ),
        MainNavigationShortcutDefinition(
            type = MainNavigationShortcutType.SEASON,
            category = MainNavigationShortcutCategory.DISCOVER,
            shortcuts = SeasonShortcutMode.entries.map(MainNavigationShortcut::Season),
        ),
        MainNavigationShortcutDefinition(
            type = MainNavigationShortcutType.CHART,
            category = MainNavigationShortcutCategory.DISCOVER,
            shortcuts = ChartType.entries.map(MainNavigationShortcut::Chart),
        ),
    )

    fun definition(type: MainNavigationShortcutType): MainNavigationShortcutDefinition =
        definitions.first { it.type == type }

    fun isRegistered(shortcut: MainNavigationShortcut): Boolean =
        definitions.any { shortcut in it.shortcuts }
}

val MainNavigationDestination.iconKey: MainNavigationIconKey
    get() = when (this) {
        MainNavigationDestination.HOME -> MainNavigationIconKey.HOME
        MainNavigationDestination.ANIME -> MainNavigationIconKey.ANIME
        MainNavigationDestination.MANGA -> MainNavigationIconKey.MANGA
        MainNavigationDestination.PROFILE -> MainNavigationIconKey.PROFILE
        MainNavigationDestination.EXPLORE -> MainNavigationIconKey.EXPLORE
        MainNavigationDestination.CALENDAR -> MainNavigationIconKey.CALENDAR
    }

val MainNavigationShortcut.iconKey: MainNavigationIconKey
    get() = when (this) {
        is MainNavigationShortcut.CurrentList -> when (type) {
            CurrentListType.AIRING -> MainNavigationIconKey.AIRING
            CurrentListType.BEHIND -> MainNavigationIconKey.BEHIND
            CurrentListType.ANIME -> MainNavigationIconKey.WATCHING
            CurrentListType.MANGA -> MainNavigationIconKey.READING
            CurrentListType.NEXT_SEASON -> MainNavigationIconKey.NEXT_SEASON
        }

        is MainNavigationShortcut.Season -> when (mode) {
            SeasonShortcutMode.CURRENT -> MainNavigationIconKey.SEASON
            SeasonShortcutMode.NEXT -> MainNavigationIconKey.NEXT_SEASON
        }

        is MainNavigationShortcut.Chart -> when (type) {
            ChartType.TOP_ANIME,
            ChartType.TOP_MANGA -> MainNavigationIconKey.TOP

            ChartType.POPULAR_ANIME,
            ChartType.POPULAR_MANGA -> MainNavigationIconKey.POPULAR

            ChartType.UPCOMING_ANIME,
            ChartType.UPCOMING_MANGA,
            ChartType.AIRING_ANIME -> MainNavigationIconKey.UPCOMING

            ChartType.TOP_MOVIES -> MainNavigationIconKey.MOVIES
            ChartType.PUBLISHING_MANGA -> MainNavigationIconKey.PUBLISHING
        }
    }

val MainNavigationItem.entry: MainNavigationEntry
    get() = destination?.let(MainNavigationEntry::Static)
        ?: MainNavigationEntry.Shortcut(requireNotNull(shortcut))

val MainNavigationItem.iconKey: MainNavigationIconKey
    get() = entry.iconKey

val MainNavigationShortcut.type: MainNavigationShortcutType
    get() = when (this) {
        is MainNavigationShortcut.CurrentList -> MainNavigationShortcutType.CURRENT_LIST
        is MainNavigationShortcut.Season -> MainNavigationShortcutType.SEASON
        is MainNavigationShortcut.Chart -> MainNavigationShortcutType.CHART
    }
