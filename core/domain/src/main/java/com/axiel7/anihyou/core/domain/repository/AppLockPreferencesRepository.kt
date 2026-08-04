package com.axiel7.anihyou.core.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.axiel7.anihyou.core.model.security.AppLockPreferences
import com.axiel7.anihyou.core.model.security.AppLockTimeout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AppLockPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    val enabled: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[APP_LOCK_ENABLED_KEY] ?: false }
        .distinctUntilChanged()

    val timeout: Flow<AppLockTimeout> = dataStore.data
        .map { preferences ->
            AppLockTimeout.fromStoredValue(preferences[APP_LOCK_TIMEOUT_KEY])
        }
        .distinctUntilChanged()

    val preferences: Flow<AppLockPreferences> = combine(enabled, timeout) { enabled, timeout ->
        AppLockPreferences(enabled = enabled, timeout = timeout)
    }.distinctUntilChanged()

    suspend fun setEnabled(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK_ENABLED_KEY] = value
        }
    }

    suspend fun setTimeout(value: AppLockTimeout) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK_TIMEOUT_KEY] = value.name
        }
    }

    private companion object {
        val APP_LOCK_ENABLED_KEY = booleanPreferencesKey("app_lock_enabled")
        val APP_LOCK_TIMEOUT_KEY = stringPreferencesKey("app_lock_timeout")
    }
}
