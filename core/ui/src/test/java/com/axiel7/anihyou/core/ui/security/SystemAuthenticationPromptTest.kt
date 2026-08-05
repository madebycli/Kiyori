package com.axiel7.anihyou.core.ui.security

import androidx.biometric.BiometricPrompt
import kotlin.test.assertEquals
import org.junit.Test

class SystemAuthenticationPromptTest {
    @Test
    fun `user and system cancellation stay non fatal`() {
        assertEquals(
            SystemAuthenticationError.CANCELED,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_USER_CANCELED),
        )
        assertEquals(
            SystemAuthenticationError.CANCELED,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_CANCELED),
        )
        assertEquals(
            SystemAuthenticationError.CANCELED,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_NEGATIVE_BUTTON),
        )
    }

    @Test
    fun `temporary and permanent lockout are explicit`() {
        assertEquals(
            SystemAuthenticationError.LOCKED_OUT,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_LOCKOUT),
        )
        assertEquals(
            SystemAuthenticationError.LOCKED_OUT,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_LOCKOUT_PERMANENT),
        )
    }

    @Test
    fun `hardware and enrollment failures are unavailable`() {
        assertEquals(
            SystemAuthenticationError.UNAVAILABLE,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_HW_NOT_PRESENT),
        )
        assertEquals(
            SystemAuthenticationError.UNAVAILABLE,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_HW_UNAVAILABLE),
        )
        assertEquals(
            SystemAuthenticationError.UNAVAILABLE,
            systemAuthenticationErrorFor(BiometricPrompt.ERROR_NO_BIOMETRICS),
        )
    }

    @Test
    fun `unclassified framework errors stay unknown`() {
        assertEquals(SystemAuthenticationError.UNKNOWN, systemAuthenticationErrorFor(Int.MAX_VALUE))
    }
}
