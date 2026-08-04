package com.axiel7.anihyou.core.model.navigation

import com.axiel7.anihyou.core.model.CurrentListType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainNavigationConfigTest {
    @Test
    fun `defaults keep Home visible and Profile hidden`() {
        val config = defaultMainNavigationConfig()

        assertTrue(config.items.single { it.destination == MainNavigationDestination.HOME }.visible)
        assertFalse(config.items.single { it.destination == MainNavigationDestination.PROFILE }.visible)
        assertEquals(4, config.visibleItems.size)
    }

    @Test
    fun `legacy visible Profile is preserved`() {
        val config = MainNavigationConfigCodec.decode("home,anime,profile")

        assertTrue(config.items.single { it.destination == MainNavigationDestination.PROFILE }.visible)
    }

    @Test
    fun `normalization keeps one Season shortcut and no more than five visible items`() {
        val config = MainNavigationConfig(
            items = MainNavigationDestination.entries.map {
                MainNavigationItem(it.stableId, true, destination = it)
            } + listOf(
                MainNavigationItem(
                    stableId = "shortcut_season_current",
                    visible = true,
                    shortcut = MainNavigationShortcut.Season(SeasonShortcutMode.CURRENT),
                ),
                MainNavigationItem(
                    stableId = "shortcut_season_next",
                    visible = true,
                    shortcut = MainNavigationShortcut.Season(SeasonShortcutMode.NEXT),
                ),
            ),
        ).normalized()

        assertEquals(1, config.items.count { it.shortcut is MainNavigationShortcut.Season })
        assertEquals(MAX_VISIBLE_MAIN_DESTINATIONS, config.visibleItems.size)
        assertTrue(config.visibleItems.any { it.destination == MainNavigationDestination.HOME })
    }

    @Test
    fun `a shortcut cannot be added twice`() {
        val shortcut = MainNavigationShortcut.CurrentList(CurrentListType.AIRING)
        val config = defaultMainNavigationConfig().addShortcut(shortcut).addShortcut(shortcut)

        assertEquals(1, config.items.count { it.shortcut == shortcut })
    }
}
