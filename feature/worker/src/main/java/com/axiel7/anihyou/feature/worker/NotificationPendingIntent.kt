package com.axiel7.anihyou.feature.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.axiel7.anihyou.core.model.notification.GenericNotification
import com.axiel7.anihyou.core.model.notification.deepLinkTarget
import com.axiel7.anihyou.core.model.notification.toAniListUri

internal fun Context.pendingIntentFor(notification: GenericNotification): PendingIntent? {
    val target = notification.deepLinkTarget() ?: return null
    val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(target.toAniListUri())
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
            Intent.FLAG_ACTIVITY_SINGLE_TOP
    } ?: return null

    return PendingIntent.getActivity(
        this,
        notification.id,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}
