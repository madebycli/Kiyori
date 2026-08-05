package com.axiel7.anihyou.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axiel7.anihyou.core.domain.repository.DefaultPreferencesRepository
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationItem
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcut
import com.axiel7.anihyou.core.model.navigation.move
import com.axiel7.anihyou.core.model.navigation.setVisibility
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/** Keeps every editor mutation routed through the persisted config normalizer. */
class MainNavigationSettingsViewModel(
    private val defaultPreferencesRepository: DefaultPreferencesRepository,
) : ViewModel() {
    val config = defaultPreferencesRepository.mainNavigationConfig

    fun setVisibility(item: MainNavigationItem, visible: Boolean) = update {
        it.setVisibility(item, visible)
    }

    fun move(fromIndex: Int, toIndex: Int) = update {
        it.move(fromIndex, toIndex)
    }

    fun remove(shortcut: MainNavigationShortcut) = update {
        it.removeShortcut(shortcut.stableId)
    }

    fun add(shortcut: MainNavigationShortcut) = update {
        it.addShortcut(shortcut)
    }

    fun reset() = update { it.reset() }

    private fun update(transform: (MainNavigationConfig) -> MainNavigationConfig) {
        viewModelScope.launch {
            defaultPreferencesRepository.setMainNavigationConfig(transform(config.first()))
        }
    }
}
