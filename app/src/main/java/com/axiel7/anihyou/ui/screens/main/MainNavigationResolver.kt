package com.axiel7.anihyou.ui.screens.main

import androidx.navigation3.runtime.NavKey
import com.axiel7.anihyou.core.model.CurrentListType
import com.axiel7.anihyou.core.model.media.ChartType
import com.axiel7.anihyou.core.model.media.currentAnimeSeason
import com.axiel7.anihyou.core.model.media.nextAnimeSeason
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationDestination
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcut
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcutRegistry
import com.axiel7.anihyou.core.model.navigation.SeasonShortcutMode
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.common.BottomDestination
import com.axiel7.anihyou.core.ui.common.navigation.Route
import java.time.LocalDateTime

/** Stable projection of Kiyori's persisted configurable navigation onto Navigation3 routes. */
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

    fun routes(config: MainNavigationConfig): Set<NavKey> = destinations(config)
        .mapTo(linkedSetOf()) { it.route }

    fun allRoutes(): Set<NavKey> = buildSet {
        addAll(BottomDestination.routes)
        MainNavigationShortcutRegistry.definitions
            .flatMap { it.shortcuts }
            .forEachIndexed { index, shortcut -> add(shortcut.asBottomDestination(index).route) }
    }

    private fun MainNavigationShortcut.asBottomDestination(index: Int): BottomDestination.Shortcut = when (this) {
        is MainNavigationShortcut.CurrentList -> BottomDestination.Shortcut(
            stableId = stableId,
            index = index,
            route = Route.CurrentFullListMain(type),
            title = when (type) {
                CurrentListType.AIRING -> R.string.airing
                CurrentListType.BEHIND -> R.string.anime_behind
                CurrentListType.ANIME -> R.string.watching
                CurrentListType.MANGA -> R.string.reading
                CurrentListType.NEXT_SEASON -> R.string.next_season
            },
            icon = when (type) {
                CurrentListType.AIRING -> R.drawable.live_tv_24
                CurrentListType.BEHIND -> R.drawable.schedule_24
                CurrentListType.ANIME -> R.drawable.play_arrow_24
                CurrentListType.MANGA -> R.drawable.book_24
                CurrentListType.NEXT_SEASON -> R.drawable.calendar_today_24
            },
        )

        is MainNavigationShortcut.Chart -> BottomDestination.Shortcut(
            stableId = stableId,
            index = index,
            route = Route.MediaChartListMain(type.name),
            title = when (type) {
                ChartType.TOP_ANIME, ChartType.TOP_MANGA -> R.string.top_100
                ChartType.POPULAR_ANIME, ChartType.POPULAR_MANGA -> R.string.top_popular
                ChartType.UPCOMING_ANIME, ChartType.UPCOMING_MANGA -> R.string.upcoming
                ChartType.AIRING_ANIME -> R.string.airing
                ChartType.TOP_MOVIES -> R.string.top_movies
                ChartType.PUBLISHING_MANGA -> R.string.publishing
            },
            icon = when (type) {
                ChartType.TOP_ANIME, ChartType.TOP_MANGA -> R.drawable.star_24
                ChartType.POPULAR_ANIME, ChartType.POPULAR_MANGA -> R.drawable.trending_up_24
                ChartType.UPCOMING_ANIME, ChartType.UPCOMING_MANGA, ChartType.PUBLISHING_MANGA -> R.drawable.schedule_24
                ChartType.AIRING_ANIME -> R.drawable.live_tv_24
                ChartType.TOP_MOVIES -> R.drawable.movie_24
            },
        )

        is MainNavigationShortcut.Season -> {
            val season = if (mode == SeasonShortcutMode.CURRENT) {
                LocalDateTime.now().currentAnimeSeason()
            } else {
                LocalDateTime.now().nextAnimeSeason()
            }
            BottomDestination.Shortcut(
                stableId = stableId,
                index = index,
                route = Route.SeasonAnimeMain(season.season.rawValue, season.year),
                title = if (mode == SeasonShortcutMode.CURRENT) R.string.season else R.string.next_season,
                icon = R.drawable.calendar_today_24,
            )
        }
    }
}
