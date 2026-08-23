package com.axiel7.anihyou.feature.settings

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.axiel7.anihyou.core.common.utils.ContextUtils.getActivity
import com.axiel7.anihyou.core.model.security.AppLockTimeout
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.composables.ListPreference
import com.axiel7.anihyou.core.ui.composables.SwitchPreference
import com.axiel7.anihyou.core.ui.security.SystemAuthenticationAvailability
import com.axiel7.anihyou.core.ui.security.SystemAuthenticationError
import com.axiel7.anihyou.core.ui.security.SystemAuthenticationPrompt

@Composable
fun AppLockSettings(
    uiState: SettingsUiState,
    event: SettingsEvent?,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
) {
    val activity = LocalContext.current.getActivity() as? FragmentActivity
    val authenticationPrompt = remember(activity) { activity?.let(::SystemAuthenticationPrompt) }
    val availability = remember(authenticationPrompt) {
        authenticationPrompt?.availability() ?: SystemAuthenticationAvailability.UNSUPPORTED
    }
    val available = availability == SystemAuthenticationAvailability.AVAILABLE
    var authenticationError by remember { mutableStateOf<String?>(null) }

    val enableTitle = stringResource(R.string.app_lock_enable_authentication)
    val disableTitle = stringResource(R.string.app_lock_disable_authentication)
    val promptSubtitle = stringResource(R.string.app_lock_unlock_subtitle)
    val genericError = stringResource(R.string.app_lock_authentication_failed)

    val availabilityMessage = when (availability) {
        SystemAuthenticationAvailability.AVAILABLE -> stringResource(R.string.app_lock_summary)
        SystemAuthenticationAvailability.NONE_ENROLLED -> stringResource(R.string.app_lock_none_enrolled)
        SystemAuthenticationAvailability.NO_HARDWARE -> stringResource(R.string.app_lock_no_hardware)
        SystemAuthenticationAvailability.TEMPORARILY_UNAVAILABLE -> stringResource(R.string.app_lock_temporarily_unavailable)
        SystemAuthenticationAvailability.UNSUPPORTED -> stringResource(R.string.app_lock_unsupported)
    }

    SwitchPreference(
        title = stringResource(R.string.app_lock),
        subtitle = authenticationError ?: availabilityMessage,
        preferenceValue = uiState.appLockEnabled,
        icon = R.drawable.lock_24,
        modifier = modifier,
        shape = shape,
        onValueChange = { enabled ->
            authenticationError = null
            if (!available || authenticationPrompt == null) {
                authenticationError = availabilityMessage
            } else {
                authenticationPrompt.prompt(
                    title = if (enabled) enableTitle else disableTitle,
                    subtitle = promptSubtitle,
                    onSuccess = { event?.setAppLockEnabledAfterAuthentication(enabled) },
                    onError = { error, message ->
                        if (error != SystemAuthenticationError.CANCELED) {
                            authenticationError = message?.toString() ?: genericError
                        }
                    },
                )
            }
        },
    )

    if (uiState.appLockEnabled) {
        ListPreference(
            title = stringResource(R.string.app_lock_timeout),
            values = AppLockTimeout.entries,
            preferenceValue = uiState.appLockTimeout,
            icon = R.drawable.schedule_24,
            labelForValue = { timeout -> timeout.localizedLabel() },
            onValueChange = { timeout -> event?.setAppLockTimeout(timeout) },
            shape = shape,
        )
    }

    authenticationError?.let { message ->
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun AppLockTimeout.localizedLabel(): String = stringResource(
    when (this) {
        AppLockTimeout.IMMEDIATELY -> R.string.app_lock_timeout_immediately
        AppLockTimeout.ONE_MINUTE -> R.string.app_lock_timeout_one_minute
        AppLockTimeout.FIVE_MINUTES -> R.string.app_lock_timeout_five_minutes
        AppLockTimeout.FIFTEEN_MINUTES -> R.string.app_lock_timeout_fifteen_minutes
        AppLockTimeout.THIRTY_MINUTES -> R.string.app_lock_timeout_thirty_minutes
    }
)
