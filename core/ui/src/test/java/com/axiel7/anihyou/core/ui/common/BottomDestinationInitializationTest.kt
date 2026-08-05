package com.axiel7.anihyou.core.ui.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BottomDestinationInitializationTest {
    @Test
    fun allStaticDestinationsInitializeWithoutStartupCrash() {
        val destinations = BottomDestination.values

        assertEquals(6, destinations.size)
        assertEquals(6, destinations.map { it.index }.distinct().size)
        assertEquals(6, BottomDestination.routes.size)
        assertEquals(4, BottomDestination.railValues.size)
        assertTrue(destinations.first() === BottomDestination.Home)
        destinations.forEach { assertNotNull(it.route) }
    }
}
