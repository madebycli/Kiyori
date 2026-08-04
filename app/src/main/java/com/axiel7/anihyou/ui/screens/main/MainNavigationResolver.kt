package com.axiel7.anihyou.ui.screens.main

import androidx.navigation3.runtime.NavKey
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationDestination
import com.axiel7.anihyou.core.ui.common.BottomDestination

/**
 * The one phone navigation projection used by both compact and wide navigation.
 * Dynamic shortcuts are deliberately kept out until their existing screen hosts are wired in Gate 4.
 */
object MainNavigationResolver {
    fun destinations(config: MainNavigationConfig): List<BottomDestination> = config.normalized()
        .visibleItems
        .mapNotNull { item ->
            when (item.destination) {
                MainNavigationDestination.HOME -> BottomDestination.Home
                MainNavigationDestination.ANIME -> BottomDestination.AnimeList
                MainNavigationDestination.MANGA -> BottomDestination.MangaList
                MainNavigationDestination.PROFILE -> BottomDestination.Profile
                MainNavigationDestination.EXPLORE -> BottomDestination.Explore
                MainNavigationDestination.CALENDAR -> BottomDestination.Calendar
                null -> null
            }
        }
        .ifEmpty { listOf(BottomDestination.Home) }

    fun routes(config: MainNavigationConfig): Set<NavKey> = destinations(config).mapTo(linkedSetOf()) { it.route }
}
