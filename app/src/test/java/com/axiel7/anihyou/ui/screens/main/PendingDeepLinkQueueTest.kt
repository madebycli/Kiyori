package com.axiel7.anihyou.ui.screens.main

import com.axiel7.anihyou.core.model.DeepLink
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PendingDeepLinkQueueTest {
    private val media = DeepLink(DeepLink.Type.ANIME, "42")
    private val search = DeepLink(DeepLink.Type.SEARCH, "search")

    @Test
    fun destinationRemainsBufferedUntilTheMatchingNavigationAcknowledgesIt() {
        val queue = PendingDeepLinkQueue()

        queue.offer(media)
        queue.consume(search)

        assertEquals(media, queue.pending.value)
    }

    @Test
    fun matchingAcknowledgementConsumesDestinationExactlyOnce() {
        val queue = PendingDeepLinkQueue()
        queue.offer(media)

        queue.consume(media)
        queue.consume(media)

        assertNull(queue.pending.value)
    }

    @Test
    fun nullIntentCannotDiscardAnAlreadyBufferedDestination() {
        val queue = PendingDeepLinkQueue()
        queue.offer(media)

        queue.offer(null)

        assertEquals(media, queue.pending.value)
    }

    @Test
    fun aNewIntentReplacesTheOldPendingTargetBeforeUnlock() {
        val queue = PendingDeepLinkQueue()
        queue.offer(media)

        queue.offer(search)

        assertEquals(search, queue.pending.value)
    }
}
