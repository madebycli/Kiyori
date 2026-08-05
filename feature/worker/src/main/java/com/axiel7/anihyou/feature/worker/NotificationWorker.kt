package com.axiel7.anihyou.feature.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.axiel7.anihyou.core.base.DataResult
import com.axiel7.anihyou.core.domain.repository.DefaultPreferencesRepository
import com.axiel7.anihyou.core.domain.repository.NotificationRepository
import com.axiel7.anihyou.core.domain.repository.UserRepository
import com.axiel7.anihyou.core.model.notification.NotificationInterval
import com.axiel7.anihyou.core.network.NetworkVariables
import com.axiel7.anihyou.core.network.type.NotificationType
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.utils.ImageUtils.getBitmapFromUrl
import com.axiel7.anihyou.core.ui.utils.NotificationUtils.createNotificationChannel
import com.axiel7.anihyou.core.ui.utils.NotificationUtils.notificationSmallIcon
import com.axiel7.anihyou.core.ui.utils.NotificationUtils.showNotification
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.milliseconds

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
    private val userRepository: UserRepository,
    private val notificationsRepository: NotificationRepository,
    private val defaultPreferencesRepository: DefaultPreferencesRepository,
    private val networkVariables: NetworkVariables,
) : CoroutineWorker(context, params) {

    // AniList does not expose a notification socket, so WorkManager checks at the user-selected interval.
    // App lock deliberately does not participate in this background path.
    @RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        try {
            setForegroundSafely()
            val accessToken = defaultPreferencesRepository.accessToken.firstOrNull()
                ?: return Result.failure()
            networkVariables.accessToken = accessToken

            val unreadCount = userRepository.getUnreadNotificationCount().firstOrNull()
                ?: return Result.failure()
            if (unreadCount <= 0) return Result.success()

            val result = notificationsRepository.getNewNotifications(unreadCount)
            return if (result is DataResult.Success && result.data != null) {
                val lastCreatedAt = defaultPreferencesRepository.lastNotificationCreatedAt
                    .firstOrNull() ?: 0
                val newNotifications = result.data!!.filter {
                    it.createdAt != null && it.createdAt!! > lastCreatedAt
                }
                if (newNotifications.isNotEmpty()) {
                    newNotifications.firstOrNull()?.createdAt?.let { createdAt ->
                        defaultPreferencesRepository.setLastNotificationCreatedAt(createdAt)
                    }
                }

                newNotifications.forEach { notification ->
                    val image = (notification.largeImageUrl ?: notification.imageUrl)?.let { url ->
                        applicationContext.getBitmapFromUrl(url)
                    }

                    val title = if (notification.type == NotificationType.AIRING) {
                        notification.mediaTitle() ?: notification.text
                    } else {
                        notification.text
                    }

                    val text = if (notification.type == NotificationType.AIRING) {
                        notification.numEpisode()?.let { episode -> "Episode $episode aired" }.orEmpty()
                    } else {
                        ""
                    }

                    applicationContext.showNotification(
                        notificationId = notification.id,
                        channelId = DEFAULT_CHANNEL_ID,
                        title = title,
                        text = text,
                        largeIcon = image,
                        bigPicture = image.takeIf { notification.isMedia },
                        pendingIntent = applicationContext.pendingIntentFor(notification),
                        group = "default",
                    )
                }

                if (newNotifications.size > 1) {
                    applicationContext.showNotification(
                        notificationId = 1,
                        channelId = DEFAULT_CHANNEL_ID,
                        title = "${newNotifications.size} ${applicationContext.getString(R.string.notifications)}",
                        text = "",
                        group = "default",
                        isGroupSummary = true,
                    )
                }

                Result.success()
            } else {
                Result.retry()
            }
        } catch (exception: Exception) {
            Log.e(TAG, "doWork: ", exception)
            return Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = ForegroundInfo(
        0,
        NotificationCompat.Builder(applicationContext, SYNC_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.notifications))
            .setSmallIcon(applicationContext.notificationSmallIcon())
            .setAutoCancel(true)
            .build(),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        } else {
            0
        },
    )

    private suspend fun setForegroundSafely() {
        try {
            setForeground(getForegroundInfo())
            delay(500.milliseconds)
        } catch (exception: IllegalStateException) {
            Log.e(TAG, "setForegroundSafely: ", exception)
        }
    }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val WORK_NAME = "default_notifications"

        const val DEFAULT_CHANNEL_ID = "default_channel_id"
        const val SYNC_CHANNEL_ID = "sync_channel_id"

        fun Context.createDefaultNotificationChannels() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(
                    id = DEFAULT_CHANNEL_ID,
                    name = getString(R.string.default_setting),
                )
                createNotificationChannel(
                    id = SYNC_CHANNEL_ID,
                    name = getString(R.string.update_interval),
                )
            }
        }

        fun WorkManager.scheduleNotificationWork(interval: NotificationInterval) {
            val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                repeatInterval = interval.value,
                repeatIntervalTimeUnit = interval.timeUnit,
                flexTimeInterval = 1,
                flexTimeIntervalUnit = TimeUnit.HOURS,
            ).apply {
                addTag(WORK_NAME)
                setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
                setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
            }.build()

            enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                notificationWorkRequest,
            )
        }

        fun WorkManager.cancelNotificationWork() {
            cancelUniqueWork(WORK_NAME)
        }
    }
}
