package com.axiel7.anihyou.core.model.notification

import com.axiel7.anihyou.core.model.DeepLink
import com.axiel7.anihyou.core.network.type.NotificationType
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.junit.Test

class NotificationDeepLinkTest {
    @Test
    fun `media and airing notifications open media details`() {
        assertEquals(
            DeepLink(DeepLink.Type.ANIME, "42"),
            notification(NotificationType.AIRING, 42).deepLinkTarget(),
        )
        assertEquals(
            DeepLink(DeepLink.Type.ANIME, "43"),
            notification(NotificationType.RELATED_MEDIA_ADDITION, 43).deepLinkTarget(),
        )
    }

    @Test
    fun `activity forum and follow notifications preserve their internal target`() {
        assertEquals(
            DeepLink(DeepLink.Type.ACTIVITY, "51"),
            notification(NotificationType.ACTIVITY_REPLY, 51).deepLinkTarget(),
        )
        assertEquals(
            DeepLink(DeepLink.Type.THREAD, "52"),
            notification(NotificationType.THREAD_COMMENT_REPLY, 52).deepLinkTarget(),
        )
        assertEquals(
            DeepLink(DeepLink.Type.USER, "53"),
            notification(NotificationType.FOLLOWING, 53).deepLinkTarget(),
        )
    }

    @Test
    fun `missing unsupported and deleted targets stay non navigable`() {
        assertNull(notification(null, 42).deepLinkTarget())
        assertNull(notification(NotificationType.MEDIA_DELETION, 0).deepLinkTarget())
        assertNull(notification(NotificationType.MEDIA_SUBMISSION_UPDATE, 42).deepLinkTarget())
    }

    @Test
    fun `target serializes to the existing AniList deep link contract`() {
        assertEquals(
            "https://anilist.co/thread/77",
            DeepLink(DeepLink.Type.THREAD, "77").toAniListUri(),
        )
    }

    private fun notification(type: NotificationType?, contentId: Int) = GenericNotification(
        id = 1,
        text = "notification",
        imageUrl = null,
        contentId = contentId,
        type = type,
        createdAt = 1,
    )
}
