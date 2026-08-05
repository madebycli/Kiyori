package com.axiel7.anihyou.core.model.navigation

import com.axiel7.anihyou.core.model.CurrentListType
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class MainNavigationConfigTest {
    @Test
    fun `v4 default is Home Anime Manga Explore Calendar`() {
        val config = defaultMainNavigationConfig()

        assertEquals(
            listOf(
                MainNavigationDestination.HOME,
                MainNavigationDestination.ANIME,
                MainNavigationDestination.MANGA,
                MainNavigationDestination.EXPLORE,
                MainNavigationDestination.CALENDAR,
            ),
            config.visibleItems.mapNotNull(MainNavigationItem::destination),
        )
        assertFalse(config.items.single { it.destination == MainNavigationDestination.PROFILE }.visible)
        assertEquals(MAIN_NAVIGATION_CONFIG_VERSION, config.version)
    }

    @Test
    fun `blank storage uses v4 default`() {
        assertEquals(defaultMainNavigationConfig(), MainNavigationConfigCodec.decode(null))
        assertEquals(defaultMainNavigationConfig(), MainNavigationConfigCodec.decode(""))
    }

    @Test
    fun `untouched v3 default migrates to v4 default`() {
        val config = MainNavigationConfigCodec.decode(
            "v3;home:1,anime:1,manga:1,profile:0,explore:1,calendar:0"
        )

        assertEquals(defaultMainNavigationConfig(), config)
    }

    @Test
    fun `custom v3 order visibility and Profile are preserved`() {
        val config = MainNavigationConfigCodec.decode(
            "v3;home:1,profile:1,manga:1,anime:0,explore:0,calendar:1"
        )

        assertEquals(
            listOf("home", "profile", "manga", "anime", "explore", "calendar"),
            config.items.map(MainNavigationItem::stableId),
        )
        assertTrue(config.items.single { it.destination == MainNavigationDestination.PROFILE }.visible)
        assertFalse(config.items.single { it.destination == MainNavigationDestination.ANIME }.visible)
        assertTrue(config.items.single { it.destination == MainNavigationDestination.CALENDAR }.visible)
    }

    @Test
    fun `five visible custom v3 tabs are not displaced by Calendar`() {
        val config = MainNavigationConfigCodec.decode(
            "v3;home:1,anime:1,manga:1,profile:1,explore:1,calendar:0"
        )

        assertEquals(5, config.visibleItems.size)
        assertFalse(config.items.single { it.destination == MainNavigationDestination.CALENDAR }.visible)
        assertTrue(config.items.single { it.destination == MainNavigationDestination.PROFILE }.visible)
    }

    @Test
    fun `v3 shortcuts survive migration`() {
        val raw = "v3;home:1,anime:1,manga:0,profile:0,explore:1,calendar:0," +
            "shortcut_current_list_airing:1"

        val config = MainNavigationConfigCodec.decode(raw)

        assertTrue(config.items.any {
            it.shortcut == MainNavigationShortcut.CurrentList(CurrentListType.AIRING) && it.visible
        })
    }

    @Test
    fun `reset uses v4 default`() {
        val custom = MainNavigationConfigCodec.decode(
            "v4;home:1,profile:1,manga:1,anime:0,explore:0,calendar:0"
        )

        assertEquals(defaultMainNavigationConfig(), custom.reset())
    }

    @Test
    fun `normalization repairs duplicates keeps one Season and no more than five visible`() {
        val config = MainNavigationConfig(
            items = MainNavigationDestination.entries.map {
                MainNavigationItem(it.stableId, true, destination = it)
            } + listOf(
                MainNavigationItem(
                    stableId = "calendar",
                    visible = true,
                    destination = MainNavigationDestination.CALENDAR,
                ),
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

        assertEquals(1, config.items.count { it.destination == MainNavigationDestination.CALENDAR })
        assertEquals(1, config.items.count { it.shortcut is MainNavigationShortcut.Season })
        assertEquals(MAX_VISIBLE_MAIN_DESTINATIONS, config.visibleItems.size)
        assertTrue(config.visibleItems.any { it.destination == MainNavigationDestination.HOME })
    }

    @Test
    fun `Home remains mandatory and visible count stays between two and five`() {
        val config = defaultMainNavigationConfig()
            .withVisibility("anime", false)
            .withVisibility("manga", false)
            .withVisibility("explore", false)
            .withVisibility("calendar", false)
            .withVisibility("home", false)

        assertTrue(config.items.single { it.destination == MainNavigationDestination.HOME }.visible)
        assertTrue(config.visibleItems.size in MIN_VISIBLE_MAIN_DESTINATIONS..MAX_VISIBLE_MAIN_DESTINATIONS)
    }

    @Test
    fun `a shortcut cannot be added twice`() {
        val shortcut = MainNavigationShortcut.CurrentList(CurrentListType.AIRING)
        val config = defaultMainNavigationConfig().addShortcut(shortcut).addShortcut(shortcut)

        assertEquals(1, config.items.count { it.shortcut == shortcut })
    }
}
