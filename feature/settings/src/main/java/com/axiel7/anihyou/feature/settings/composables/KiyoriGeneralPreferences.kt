package com.axiel7.anihyou.feature.settings.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.common.LocalNavActionManager
import com.axiel7.anihyou.core.ui.composables.PlainPreference
import com.axiel7.anihyou.core.ui.composables.middleShape
import com.axiel7.anihyou.feature.settings.AppLockSettings
import com.axiel7.anihyou.feature.settings.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

/** Kiyori-owned preferences embedded into upstream's redesigned General section. */
@Composable
fun KiyoriGeneralPreferences() {
    val navActionManager = LocalNavActionManager.current
    val viewModel: SettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlainPreference(
        title = stringResource(R.string.main_navigation),
        subtitle = stringResource(R.string.main_navigation_summary),
        icon = R.drawable.sort_24,
        onClick = navActionManager::toMainNavigationSettings,
        shape = middleShape,
    )

    AppLockSettings(
        uiState = uiState,
        event = viewModel,
        shape = middleShape,
    )
}
