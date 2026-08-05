package com.axiel7.anihyou.core.ui.security

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

private const val ALLOWED_AUTHENTICATORS =
    BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL

enum class SystemAuthenticationAvailability {
    AVAILABLE,
    NONE_ENROLLED,
    NO_HARDWARE,
    TEMPORARILY_UNAVAILABLE,
    UNSUPPORTED,
}

enum class SystemAuthenticationError {
    CANCELED,
    LOCKED_OUT,
    UNAVAILABLE,
    UNKNOWN,
}

internal fun systemAuthenticationErrorFor(errorCode: Int): SystemAuthenticationError = when (errorCode) {
    BiometricPrompt.ERROR_CANCELED,
    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
    BiometricPrompt.ERROR_USER_CANCELED -> SystemAuthenticationError.CANCELED

    BiometricPrompt.ERROR_LOCKOUT,
    BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> SystemAuthenticationError.LOCKED_OUT

    BiometricPrompt.ERROR_HW_NOT_PRESENT,
    BiometricPrompt.ERROR_HW_UNAVAILABLE,
    BiometricPrompt.ERROR_NO_BIOMETRICS -> SystemAuthenticationError.UNAVAILABLE

    else -> SystemAuthenticationError.UNKNOWN
}

class SystemAuthenticationPrompt(
    private val activity: FragmentActivity,
) {
    fun availability(): SystemAuthenticationAvailability = when (
        BiometricManager.from(activity).canAuthenticate(ALLOWED_AUTHENTICATORS)
    ) {
        BiometricManager.BIOMETRIC_SUCCESS -> SystemAuthenticationAvailability.AVAILABLE
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
            SystemAuthenticationAvailability.NONE_ENROLLED
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
            SystemAuthenticationAvailability.NO_HARDWARE
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
            SystemAuthenticationAvailability.TEMPORARILY_UNAVAILABLE
        else -> SystemAuthenticationAvailability.UNSUPPORTED
    }

    fun prompt(
        title: String,
        subtitle: String? = null,
        onSuccess: () -> Unit,
        onError: (SystemAuthenticationError, CharSequence?) -> Unit,
    ) {
        if (availability() != SystemAuthenticationAvailability.AVAILABLE) {
            onError(SystemAuthenticationError.UNAVAILABLE, null)
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    onSuccess()
                }

                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    onError(systemAuthenticationErrorFor(errorCode), errString)
                }
            },
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setAllowedAuthenticators(ALLOWED_AUTHENTICATORS)
            .apply {
                if (!subtitle.isNullOrBlank()) setSubtitle(subtitle)
            }
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
