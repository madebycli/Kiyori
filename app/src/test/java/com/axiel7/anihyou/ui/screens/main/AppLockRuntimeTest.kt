package com.axiel7.anihyou.ui.screens.main

import com.axiel7.anihyou.core.model.security.AppLockPreferences
import com.axiel7.anihyou.core.model.security.AppLockTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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
    fun disabledLockStartsAndStaysUnlocked() {
        val runtime = AppLockRuntime(MonotonicClock { 0L })

        runtime.initialize(AppLockPreferences(enabled = false))
        runtime.onProcessBackgrounded()
        runtime.onProcessForegrounded()

        assertFalse(runtime.state.value.enabled)
        assertFalse(runtime.state.value.locked)
        assertNull(runtime.state.value.backgroundedAtMillis)
    }

    @Test
    fun immediateTimeoutLocksOnNextRealProcessForeground() {
        var now = 1_000L
        val runtime = unlockedRuntime(AppLockTimeout.IMMEDIATELY) { now }

        runtime.onProcessBackgrounded()
        runtime.onProcessForegrounded()

        assertTrue(runtime.state.value.locked)
    }

    @Test
    fun eachDelayedTimeoutLocksAtBoundaryButNotOneMillisecondBefore() {
        AppLockTimeout.entries
            .filterNot { it == AppLockTimeout.IMMEDIATELY }
            .forEach { timeout ->
                var now = 10_000L
                val runtime = unlockedRuntime(timeout) { now }

                runtime.onProcessBackgrounded()
                now += timeout.durationMillis - 1L
                runtime.onProcessForegrounded()
                assertFalse("$timeout locked before its boundary", runtime.state.value.locked)

                runtime.onProcessBackgrounded()
                now += timeout.durationMillis
                runtime.onProcessForegrounded()
                assertTrue("$timeout did not lock at its boundary", runtime.state.value.locked)
            }
    }

    @Test
    fun foregroundWithoutProcessBackgroundDoesNotRelockForRotationNavigationOrPrompt() {
        val runtime = unlockedRuntime(AppLockTimeout.IMMEDIATELY) { 5_000L }

        repeat(3) { runtime.onProcessForegrounded() }

        assertFalse(runtime.state.value.locked)
        assertNull(runtime.state.value.backgroundedAtMillis)
    }

    @Test
    fun monotonicClockRegressionCannotCreateAFalseTimeout() {
        var now = 10_000L
        val runtime = unlockedRuntime(AppLockTimeout.ONE_MINUTE) { now }

        runtime.onProcessBackgrounded()
        now = 1_000L
        runtime.onProcessForegrounded()

        assertFalse(runtime.state.value.locked)
    }

    @Test
    fun disablingAppLockAlwaysClearsLockedAndBackgroundState() {
        var now = 0L
        val runtime = AppLockRuntime(MonotonicClock { now })
        runtime.initialize(AppLockPreferences(enabled = true))
        runtime.onAuthenticationSucceeded()
        runtime.onProcessBackgrounded()
        now += 1_000L

        runtime.updatePreferences(AppLockPreferences(enabled = false))

        assertFalse(runtime.state.value.enabled)
        assertFalse(runtime.state.value.locked)
        assertNull(runtime.state.value.backgroundedAtMillis)
    }

    @Test
    fun enablingAppLockRequiresAFreshAuthentication() {
        val runtime = AppLockRuntime(MonotonicClock { 0L })
        runtime.initialize(AppLockPreferences(enabled = false))

        runtime.updatePreferences(AppLockPreferences(enabled = true))

        assertTrue(runtime.state.value.enabled)
        assertTrue(runtime.state.value.locked)
    }

    @Test
    fun repeatedInitializationCannotUnlockAColdStart() {
        val runtime = AppLockRuntime(MonotonicClock { 0L })
        runtime.initialize(AppLockPreferences(enabled = true))

        runtime.initialize(AppLockPreferences(enabled = false))

        assertTrue(runtime.state.value.enabled)
        assertTrue(runtime.state.value.locked)
    }

    private fun unlockedRuntime(
        timeout: AppLockTimeout,
        now: () -> Long,
    ) = AppLockRuntime(MonotonicClock(now)).apply {
        initialize(AppLockPreferences(enabled = true, timeout = timeout))
        onAuthenticationSucceeded()
    }
}
