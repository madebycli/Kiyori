package com.axiel7.anihyou.ui.screens.main

import com.axiel7.anihyou.core.model.navigation.MainNavigationDestination
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcutRegistry
import com.axiel7.anihyou.core.model.navigation.defaultMainNavigationConfig
import com.axiel7.anihyou.core.ui.common.BottomDestination
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MainNavigationResolverTest {
    @Test
    fun routeUniverseContainsEveryStaticDestination() {
        val allRoutes = MainNavigationResolver.allRoutes()

        BottomDestination.values.forEach { destination ->
            assertTrue(
                "Missing top-level route for ${destination::class.simpleName}",
                destination.route in allRoutes,
            )
        }
    }

    @Test
    fun routeUniverseContainsEveryConfigurableShortcut() {
        val allRoutes = MainNavigationResolver.allRoutes()

        MainNavigationShortcutRegistry.definitions
            .flatMap { it.shortcuts }
            .forEach { shortcut ->
                val config = defaultMainNavigationConfig().addShortcut(shortcut)
                val destination = MainNavigationResolver.destinations(config)
                    .filterIsInstance<BottomDestination.Shortcut>()
                    .firstOrNull { it.stableId == shortcut.stableId }

                assertNotNull("Shortcut was not projected: ${shortcut.stableId}", destination)
                assertTrue(
                    "Shortcut route is outside the stable route universe: ${shortcut.stableId}",
                    destination!!.route in allRoutes,
                )
            }
    }

    @Test
    fun changingVisibleTabsOnlyChangesTheProjection() {
        val allRoutes = MainNavigationResolver.allRoutes()
        val config = defaultMainNavigationConfig()
            .withVisibility(MainNavigationDestination.EXPLORE.stableId, false)
            .withVisibility(MainNavigationDestination.PROFILE.stableId, true)
            .withVisibility(MainNavigationDestination.CALENDAR.stableId, true)

        val visibleRoutes = MainNavigationResolver.routes(config)

        assertTrue(BottomDestination.Profile.route in visibleRoutes)
        assertTrue(BottomDestination.Calendar.route in visibleRoutes)
        assertTrue(visibleRoutes.all { it in allRoutes })
    }
}
