package com.axiel7.anihyou.ui.screens.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axiel7.anihyou.core.base.ANIHYOU_AUTH_RESPONSE
import com.axiel7.anihyou.core.base.ANIHYOU_SCHEME
import com.axiel7.anihyou.core.domain.repository.AppLockPreferencesRepository
import com.axiel7.anihyou.core.domain.repository.DefaultPreferencesRepository
import com.axiel7.anihyou.core.domain.repository.LoginRepository
import com.axiel7.anihyou.core.model.DeepLink
import com.axiel7.anihyou.core.model.DefaultTab
import com.axiel7.anihyou.core.model.security.AppLockPreferences
import com.axiel7.anihyou.core.network.NetworkVariables
import com.axiel7.anihyou.core.network.type.ScoreFormat
import com.materialkolor.PaletteStyle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    private val networkVariables: NetworkVariables,
    private val loginRepository: LoginRepository,
    private val defaultPreferencesRepository: DefaultPreferencesRepository,
    private val appLockPreferencesRepository: AppLockPreferencesRepository,
    private val appLockRuntime: AppLockRuntime,
) : ViewModel(), MainEvent {

    private val pendingDeepLinks = PendingDeepLinkQueue()
    val pendingDeepLink: StateFlow<DeepLink?> = pendingDeepLinks.pending

    val accessToken = defaultPreferencesRepository.accessToken

    val isLoggedIn = defaultPreferencesRepository.isLoggedIn

    val homeTab = defaultPreferencesRepository.defaultHomeTab

    val mainNavigationConfig = defaultPreferencesRepository.mainNavigationConfig

    val theme = defaultPreferencesRepository.theme

    val useBlackColors = defaultPreferencesRepository.useBlackColors

    val appColor = defaultPreferencesRepository.appColor

    val appColorMode = defaultPreferencesRepository.appColorMode

    val paletteStyle = defaultPreferencesRepository.colorPalette.map { value ->
        value?.let { PaletteStyle.valueOf(it) } ?: PaletteStyle.Neutral
    }

    val blurAdultContent = defaultPreferencesRepository.blurAdult

    val scoreFormat = defaultPreferencesRepository.scoreFormat.map {
        it ?: ScoreFormat.POINT_10_DECIMAL
    }

    val hideScores = defaultPreferencesRepository.hideScores

    val appLockPreferences = appLockPreferencesRepository.preferences

    val appLockState = appLockRuntime.state

    override fun saveLastTab(index: Int) {
        viewModelScope.launch {
            defaultPreferencesRepository.setLastTab(index)
        }
    }

    suspend fun getStartTab(): Int {
        val defaultTab = defaultPreferencesRepository.defaultTab.first()
        return if (defaultTab == null || defaultTab == DefaultTab.LAST_USED) {
            defaultPreferencesRepository.lastTab.first()
        } else {
            defaultTab.ordinal - 1
        }
    }

    fun initializeAppLock(preferences: AppLockPreferences) {
        appLockRuntime.initialize(preferences)
    }

    fun onProcessBackgrounded() {
        appLockRuntime.onProcessBackgrounded()
    }

    fun onProcessForegrounded() {
        appLockRuntime.onProcessForegrounded()
    }

    fun onAppLockAuthenticationSucceeded() {
        appLockRuntime.onAuthenticationSucceeded()
    }

    fun queueDeepLink(deepLink: DeepLink?) {
        pendingDeepLinks.offer(deepLink)
    }

    fun consumeDeepLink(deepLink: DeepLink) {
        pendingDeepLinks.consume(deepLink)
    }

    fun setToken(token: String?) {
        networkVariables.accessToken = token
    }

    fun onIntentDataReceived(data: Uri?) = viewModelScope.launch {
        if (data?.scheme == ANIHYOU_SCHEME && data.toString().contains(ANIHYOU_AUTH_RESPONSE)) {
            loginRepository.parseRedirectUri(data)
        }
    }

    init {
        viewModelScope.launch {
            defaultPreferencesRepository.normalizeMainNavigationConfig()
        }
        accessToken
            .onEach { setToken(it) }
            .launchIn(viewModelScope)
        appLockPreferences
            .onEach(appLockRuntime::updatePreferences)
            .launchIn(viewModelScope)
    }
}
