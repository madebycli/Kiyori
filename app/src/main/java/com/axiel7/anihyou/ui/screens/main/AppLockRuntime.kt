package com.axiel7.anihyou.ui.screens.main

import android.os.SystemClock
import com.axiel7.anihyou.core.model.security.AppLockPreferences
import com.axiel7.anihyou.core.model.security.AppLockTimeout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

fun interface MonotonicClock {
    fun nowMillis(): Long
}

data class AppLockRuntimeState(
    val initialized: Boolean = false,
    val enabled: Boolean = false,
    val timeout: AppLockTimeout = AppLockTimeout.IMMEDIATELY,
    val locked: Boolean = false,
    val backgroundedAtMillis: Long? = null,
)

class AppLockRuntime(
    private val clock: MonotonicClock = MonotonicClock(SystemClock::elapsedRealtime),
) {
    private val mutableState = MutableStateFlow(AppLockRuntimeState())
    val state: StateFlow<AppLockRuntimeState> = mutableState.asStateFlow()

    fun initialize(preferences: AppLockPreferences) {
        if (mutableState.value.initialized) return
        mutableState.value = AppLockRuntimeState(
            initialized = true,
            enabled = preferences.enabled,
            timeout = preferences.timeout,
            locked = preferences.enabled,
        )
    }

    fun updatePreferences(preferences: AppLockPreferences) {
        if (!mutableState.value.initialized) {
            initialize(preferences)
            return
        }

        mutableState.update { current ->
            when {
                !preferences.enabled -> current.copy(
                    enabled = false,
                    timeout = preferences.timeout,
                    locked = false,
                    backgroundedAtMillis = null,
                )

                !current.enabled -> current.copy(
                    enabled = true,
                    timeout = preferences.timeout,
                    locked = true,
                    backgroundedAtMillis = null,
                )

                else -> current.copy(
                    enabled = true,
                    timeout = preferences.timeout,
                )
            }
        }
    }

    fun onProcessBackgrounded() {
        mutableState.update { current ->
            if (!current.enabled) current
            else current.copy(backgroundedAtMillis = clock.nowMillis())
        }
    }

    fun onProcessForegrounded() {
        mutableState.update { current ->
            if (!current.enabled) {
                current.copy(locked = false, backgroundedAtMillis = null)
            } else {
                val elapsed = current.backgroundedAtMillis
                    ?.let { (clock.nowMillis() - it).coerceAtLeast(0L) }
                val timeoutReached = elapsed != null && elapsed >= current.timeout.durationMillis
                current.copy(
                    locked = current.locked || timeoutReached,
                    backgroundedAtMillis = null,
                )
            }
        }
    }

    fun onAuthenticationSucceeded() {
        mutableState.update { current ->
            current.copy(locked = false, backgroundedAtMillis = null)
        }
    }
}
