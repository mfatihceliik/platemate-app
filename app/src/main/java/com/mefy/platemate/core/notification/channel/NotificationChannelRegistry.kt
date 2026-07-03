package com.mefy.platemate.core.notification.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.notification.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bildirim kanallarını (Android O+) tek noktadan oluşturur ve tip → kanal-id eşlemesini sağlar.
 * [ensureChannels] uygulama başlangıcında bir kez çağrılır.
 */
@Singleton
class NotificationChannelRegistry @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun ensureChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        NotificationType.entries.forEach { type ->
            manager.createNotificationChannel(
                NotificationChannel(
                    channelId(type),
                    channelName(type),
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    fun channelId(type: NotificationType): String = "platemate_${type.name.lowercase()}"

    private fun channelName(type: NotificationType): String = context.getString(
        when (type) {
            NotificationType.MESSAGE -> R.string.notification_channel_message
            NotificationType.FRIEND_REQUEST -> R.string.notification_channel_friend
            NotificationType.PLATE_REVIEW -> R.string.notification_channel_plate_review
            NotificationType.NEW_FOLLOWER -> R.string.notification_channel_new_follower
            NotificationType.SYSTEM -> R.string.notification_channel_system
        }
    )
}
