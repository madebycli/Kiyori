package com.axiel7.anihyou.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axiel7.anihyou.core.domain.repository.DefaultPreferencesRepository
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcut
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Keeps every editor mutation routed through the persisted config normalizer. */
class MainNavigationSettingsViewModel(
    private val defaultPreferencesRepository: DefaultPreferencesRepository,
) : ViewModel() {
    val config = defaultPreferencesRepository.mainNavigationConfig

    fun setVisibility(stableId: String, visible: Boolean) = update { it.withVisibility(stableId, visible) }

    fun move(stableId: String, offset: Int) = update { config ->
        val index = config.items.indexOfFirst { it.stableId == stableId }
        if (index < 0) config else config.move(stableId, index + offset)
    }

    fun remove(stableId: String) = update { it.removeShortcut(stableId) }

    fun add(shortcut: MainNavigationShortcut) = update { it.addShortcut(shortcut) }

    fun reset() = update { it.reset() }

    private fun update(transform: (MainNavigationConfig) -> MainNavigationConfig) {
        viewModelScope.launch {
            defaultPreferencesRepository.setMainNavigationConfig(transform(config.first()))
        }
    }
}
