package com.axiel7.anihyou.ui.screens.main

import com.axiel7.anihyou.core.model.DeepLink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PendingDeepLinkQueue {
    private val mutablePending = MutableStateFlow<DeepLink?>(null)
    val pending: StateFlow<DeepLink?> = mutablePending.asStateFlow()

    fun offer(deepLink: DeepLink?) {
        if (deepLink != null) mutablePending.value = deepLink
    }

    fun consume(deepLink: DeepLink) {
        if (mutablePending.value == deepLink) mutablePending.value = null
    }
}
