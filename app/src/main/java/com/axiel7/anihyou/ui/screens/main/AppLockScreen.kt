package com.axiel7.anihyou.ui.screens.main

import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.security.SystemAuthenticationError
import com.axiel7.anihyou.core.ui.security.SystemAuthenticationPrompt

@Composable
fun AppLockGate(
    activity: FragmentActivity,
    state: AppLockRuntimeState,
    onAuthenticationSucceeded: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (!state.initialized) {
        // Never render protected content while lock preferences are unresolved.
        Box(modifier = Modifier.fillMaxSize())
        return
    }
    if (!state.enabled || !state.locked) {
        content()
        return
    }

    val authenticationPrompt = remember(activity) { SystemAuthenticationPrompt(activity) }
    val title = stringResource(R.string.app_lock_unlock_title)
    val subtitle = stringResource(R.string.app_lock_unlock_subtitle)
    val genericError = stringResource(R.string.app_lock_authentication_failed)
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var automaticPromptShown by remember(state.locked) { mutableStateOf(false) }

    fun authenticate() {
        errorMessage = null
        authenticationPrompt.prompt(
            title = title,
            subtitle = subtitle,
            onSuccess = onAuthenticationSucceeded,
            onError = { error, message ->
                if (error != SystemAuthenticationError.CANCELED) {
                    errorMessage = message?.toString() ?: genericError
                }
            },
        )
    }

    DisposableEffect(activity, state.locked) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        onDispose {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    LaunchedEffect(state.locked) {
        if (!automaticPromptShown) {
            automaticPromptShown = true
            authenticate()
        }
    }

    AppLockScreen(
        errorMessage = errorMessage,
        onUnlock = ::authenticate,
    )
}

@Composable
private fun AppLockScreen(
    errorMessage: String?,
    onUnlock: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.lock_24),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.app_lock_locked_message),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.app_lock_unlock_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
            Button(onClick = onUnlock) {
                Text(stringResource(R.string.app_lock_unlock))
            }
        }
    }
}
