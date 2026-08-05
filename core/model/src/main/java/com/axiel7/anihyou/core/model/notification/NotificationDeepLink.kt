package com.axiel7.anihyou.core.model.notification

import com.axiel7.anihyou.core.model.DeepLink
import com.axiel7.anihyou.core.network.type.NotificationType

fun GenericNotification.deepLinkTarget(): DeepLink? {
    if (contentId <= 0) return null

    val targetType = when {
        type == NotificationType.AIRING ||
            NotificationTypeGroup.MEDIA.values?.contains(type) == true -> DeepLink.Type.ANIME

        NotificationTypeGroup.ACTIVITY.values?.contains(type) == true -> DeepLink.Type.ACTIVITY
        NotificationTypeGroup.FORUM.values?.contains(type) == true -> DeepLink.Type.THREAD
        NotificationTypeGroup.FOLLOWS.values?.contains(type) == true -> DeepLink.Type.USER
        else -> null
    }

    return targetType?.let { DeepLink(type = it, id = contentId.toString()) }
}

fun DeepLink.toAniListUri(): String = "https://anilist.co/${type.name.lowercase()}/$id"
