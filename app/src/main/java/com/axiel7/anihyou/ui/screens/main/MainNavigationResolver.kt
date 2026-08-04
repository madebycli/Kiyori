package com.axiel7.anihyou.ui.screens.main

import androidx.navigation3.runtime.NavKey
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationDestination
import com.axiel7.anihyou.core.model.navigation.MainNavigationItem
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcut
import com.axiel7.anihyou.core.model.navigation.SeasonShortcutMode
import com.axiel7.anihyou.core.model.media.currentAnimeSeason
import com.axiel7.anihyou.core.model.media.nextAnimeSeason
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.common.BottomDestination
import com.axiel7.anihyou.core.ui.common.navigation.Routes
import java.time.LocalDateTime

/**
 * The one phone navigation projection used by both compact and wide navigation.
 * Dynamic shortcuts are deliberately kept out until their existing screen hosts are wired in Gate 4.
 */
object MainNavigationResolver {
    fun destinations(config: MainNavigationConfig): List<BottomDestination> = config.normalized()
        .visibleItems
        .mapIndexedNotNull { index, item ->
            when (item.destination) {
                MainNavigationDestination.HOME -> BottomDestination.Home
                MainNavigationDestination.ANIME -> BottomDestination.AnimeList
                MainNavigationDestination.MANGA -> BottomDestination.MangaList
                MainNavigationDestination.PROFILE -> BottomDestination.Profile
                MainNavigationDestination.EXPLORE -> BottomDestination.Explore
                MainNavigationDestination.CALENDAR -> BottomDestination.Calendar
                null -> item.shortcut?.asBottomDestination(index)
            }
        }
        .ifEmpty { listOf(BottomDestination.Home) }

    fun routes(config: MainNavigationConfig): Set<NavKey> = destinations(config).mapTo(linkedSetOf()) { it.route }

    private fun MainNavigationShortcut.asBottomDestination(index: Int): BottomDestination.Shortcut = when (this) {
        is MainNavigationShortcut.CurrentList -> BottomDestination.Shortcut(
            stableId = stableId,
            index = index,
            route = Routes.CurrentFullList(type),
            title = when (type) {
                com.axiel7.anihyou.core.model.CurrentListType.AIRING -> R.string.airing
                com.axiel7.anihyou.core.model.CurrentListType.BEHIND -> R.string.anime_behind
                com.axiel7.anihyou.core.model.CurrentListType.ANIME -> R.string.watching
                com.axiel7.anihyou.core.model.CurrentListType.MANGA -> R.string.reading
                com.axiel7.anihyou.core.model.CurrentListType.NEXT_SEASON -> R.string.next_season
            },
            icon = R.drawable.play_arrow_24,
        )
        is MainNavigationShortcut.Chart -> BottomDestination.Shortcut(
            stableId = stableId,
            index = index,
            route = Routes.MediaChartList(type.name),
            title = when (type) {
                com.axiel7.anihyou.core.model.media.ChartType.TOP_ANIME,
                com.axiel7.anihyou.core.model.media.ChartType.TOP_MANGA -> R.string.top_100
                com.axiel7.anihyou.core.model.media.ChartType.POPULAR_ANIME,
                com.axiel7.anihyou.core.model.media.ChartType.POPULAR_MANGA -> R.string.top_popular
                com.axiel7.anihyou.core.model.media.ChartType.UPCOMING_ANIME,
                com.axiel7.anihyou.core.model.media.ChartType.UPCOMING_MANGA -> R.string.upcoming
                com.axiel7.anihyou.core.model.media.ChartType.AIRING_ANIME -> R.string.airing
                com.axiel7.anihyou.core.model.media.ChartType.TOP_MOVIES -> R.string.top_movies
                com.axiel7.anihyou.core.model.media.ChartType.PUBLISHING_MANGA -> R.string.publishing
            },
            icon = R.drawable.star_24,
        )
        is MainNavigationShortcut.Season -> {
            val season = if (mode == SeasonShortcutMode.CURRENT) LocalDateTime.now().currentAnimeSeason()
            else LocalDateTime.now().nextAnimeSeason()
            BottomDestination.Shortcut(
                stableId = stableId,
                index = index,
                route = Routes.SeasonAnime(season.season.rawValue, season.year),
                title = if (mode == SeasonShortcutMode.CURRENT) R.string.season else R.string.next_season,
                icon = R.drawable.calendar_today_24,
            )
        }
    }
}
