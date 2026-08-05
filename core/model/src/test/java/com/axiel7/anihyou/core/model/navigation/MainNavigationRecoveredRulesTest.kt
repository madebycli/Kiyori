package com.axiel7.anihyou.core.model.navigation

import com.axiel7.anihyou.core.model.CurrentListType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainNavigationRecoveredRulesTest {
    @Test
    fun homeRemainsVisibleAndCannotBeMoved() {
        val config = defaultMainNavigationConfig()
        val home = config.items.first { it.destination == MainNavigationDestination.HOME }

        val hidden = config.setVisibility(home, false)
        val moved = config.move(0, config.items.lastIndex)

        assertTrue(hidden.items.first { it.destination == MainNavigationDestination.HOME }.visible)
        assertEquals(MainNavigationDestination.HOME, moved.items.first().destination)
    }

    @Test
    fun visibilityStaysBetweenTwoAndFiveItems() {
        var config = defaultMainNavigationConfig()
        config.items
            .filter { it.destination != MainNavigationDestination.HOME }
            .forEach { item -> config = config.setVisibility(item, false) }

        assertEquals(MIN_VISIBLE_MAIN_DESTINATIONS, config.visibleItems.size)

        config = config.addShortcut(MainNavigationShortcut.CurrentList(CurrentListType.AIRING))
        config = config.addShortcut(MainNavigationShortcut.CurrentList(CurrentListType.BEHIND))
        config = config.addShortcut(MainNavigationShortcut.CurrentList(CurrentListType.ANIME))
        config = config.addShortcut(MainNavigationShortcut.CurrentList(CurrentListType.MANGA))

        assertTrue(config.visibleItems.size <= MAX_VISIBLE_MAIN_DESTINATIONS)
    }

    @Test
    fun addedShortcutIsTypedAndDiscoverable() {
        val shortcut = MainNavigationShortcut.CurrentList(CurrentListType.AIRING)
        val config = defaultMainNavigationConfig().addShortcut(shortcut)

        assertTrue(config.containsShortcut(shortcut))
        assertTrue(config.containsShortcut(MainNavigationShortcutType.CURRENT_LIST))
        assertTrue(MainNavigationShortcutRegistry.isRegistered(shortcut))
        assertFalse(config.shortcuts.isEmpty())
    }
}
