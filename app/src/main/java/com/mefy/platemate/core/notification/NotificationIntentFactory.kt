package com.mefy.platemate.core.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mefy.platemate.MainActivity
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.domain.model.notification.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bildirim tıklamasını sohbete/ekrana götüren intent köprüsü. [contentIntent] bildirimin
 * `setContentIntent`'ine konur; tıklayınca [MainActivity] açılır ve extras taşınır. [parse]
 * Activity tarafında extras'ı tekrar [AppNotification]'a çevirir (banner ile aynı nav handler'ı
 * kullanılır). Foreground/in-app banner ile tutarlı, programatik nav.
 */
@Singleton
class NotificationIntentFactory @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    fun contentIntent(notification: AppNotification): PendingIntent {
        val type = notification.type
        val referenceId = notification.referenceIdOrNull()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_TYPE, type.name)
            notification.titleOrNull()?.let { putExtra(EXTRA_TITLE, it) }
            referenceId?.let { putExtra(EXTRA_REFERENCE_ID, it) }
        }
        // requestCode hedef başına farklı olmalı: extras PendingIntent eşitliğine girmez, requestCode girer.
        val requestCode = referenceId?.toInt() ?: (REQUEST_CODE_BASE + type.ordinal)
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun parse(intent: Intent?): AppNotification? {
        val typeName = intent?.getStringExtra(EXTRA_TYPE) ?: return null
        val title = intent.getStringExtra(EXTRA_TITLE)
        val referenceId = if (intent.hasExtra(EXTRA_REFERENCE_ID)) {
            intent.getLongExtra(EXTRA_REFERENCE_ID, 0L)
        } else {
            null
        }
        return when (NotificationType.fromString(typeName)) {
            NotificationType.MESSAGE -> AppNotification.Message(
                senderName = title.orEmpty(),
                body = "",
                roomId = referenceId
            )
            NotificationType.FRIEND_REQUEST -> AppNotification.FriendRequest(title, null, referenceId)
            NotificationType.PLATE_REVIEW -> AppNotification.PlateReview(title, null, referenceId)
            NotificationType.NEW_FOLLOWER -> AppNotification.NewFollower(title, null, referenceId)
            NotificationType.SYSTEM -> AppNotification.System(title, null)
        }
    }

    private fun AppNotification.referenceIdOrNull(): Long? = when (this) {
        is AppNotification.Message -> roomId
        is AppNotification.FriendRequest -> referenceId
        is AppNotification.PlateReview -> referenceId
        is AppNotification.NewFollower -> referenceId
        is AppNotification.System -> null
    }

    private fun AppNotification.titleOrNull(): String? = when (this) {
        is AppNotification.Message -> senderName
        is AppNotification.FriendRequest -> title
        is AppNotification.PlateReview -> title
        is AppNotification.NewFollower -> title
        is AppNotification.System -> title
    }

    private companion object {
        const val EXTRA_TYPE = "pm_notif_type"
        const val EXTRA_TITLE = "pm_notif_title"
        const val EXTRA_REFERENCE_ID = "pm_notif_reference_id"
        const val REQUEST_CODE_BASE = 100_000
    }
}
