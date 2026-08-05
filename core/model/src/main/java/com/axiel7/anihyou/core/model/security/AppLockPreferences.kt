package com.axiel7.anihyou.core.model.security

data class AppLockPreferences(
    val enabled: Boolean = false,
    val timeout: AppLockTimeout = AppLockTimeout.IMMEDIATELY,
)

enum class AppLockTimeout(
    val durationMillis: Long,
) {
    IMMEDIATELY(0L),
    ONE_MINUTE(60_000L),
    FIVE_MINUTES(5L * 60_000L),
    FIFTEEN_MINUTES(15L * 60_000L),
    THIRTY_MINUTES(30L * 60_000L),
    ;

    companion object {
        fun fromStoredValue(value: String?): AppLockTimeout =
            entries.firstOrNull { it.name == value } ?: IMMEDIATELY
    }
}
