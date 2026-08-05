package com.axiel7.anihyou.ui.screens.main

import com.axiel7.anihyou.core.model.security.AppLockPreferences
import com.axiel7.anihyou.core.model.security.AppLockTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppLockRuntimeTest {
    @Test
    fun enabledLockStartsLockedAndAuthenticationUnlocksIt() {
        val runtime = AppLockRuntime(MonotonicClock { 0L })

        runtime.initialize(AppLockPreferences(enabled = true))
        assertTrue(runtime.state.value.locked)

        runtime.onAuthenticationSucceeded()
        assertFalse(runtime.state.value.locked)
    }

    @Test
    fun foregroundLocksOnlyAfterConfiguredTimeout() {
        var now = 1_000L
        val runtime = AppLockRuntime(MonotonicClock { now })
        runtime.initialize(
            AppLockPreferences(
                enabled = true,
                timeout = AppLockTimeout.FIVE_MINUTES,
            )
        )
        runtime.onAuthenticationSucceeded()

        runtime.onProcessBackgrounded()
        now += AppLockTimeout.FIVE_MINUTES.durationMillis - 1L
        runtime.onProcessForegrounded()
        assertFalse(runtime.state.value.locked)

        runtime.onProcessBackgrounded()
        now += AppLockTimeout.FIVE_MINUTES.durationMillis
        runtime.onProcessForegrounded()
        assertTrue(runtime.state.value.locked)
    }

    @Test
    fun disablingAppLockAlwaysClearsLockedState() {
        val runtime = AppLockRuntime(MonotonicClock { 0L })
        runtime.initialize(AppLockPreferences(enabled = true))

        runtime.updatePreferences(AppLockPreferences(enabled = false))

        assertFalse(runtime.state.value.enabled)
        assertFalse(runtime.state.value.locked)
    }
}
