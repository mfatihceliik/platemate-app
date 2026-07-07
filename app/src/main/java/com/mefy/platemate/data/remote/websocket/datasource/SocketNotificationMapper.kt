package com.mefy.platemate.data.remote.websocket.datasource

import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.domain.model.notification.NotificationType
import javax.inject.Inject
import javax.inject.Singleton
import org.json.JSONObject

/**
 * `notification_received` socket payload'ını ([com.mefy.platemate.business...NotificationSignalDto]
 * karşılığı) tip-bazlı [AppNotification]'a çevirir. FCM'deki
 * [com.mefy.platemate.data.fcm.FcmNotificationMapper] ile aynı modeli üretir; tanınmayan tip
 * SYSTEM'e düşer, gösterilecek içerik yoksa null döner.
 */
@Singleton
class SocketNotificationMapper @Inject constructor() {

    fun map(payload: JSONObject): AppNotification? {
        val title = payload.optNullableString("title")
        val content = payload.optNullableString("content")
        val referenceId = payload.optNullableLong("referenceId")

        if (title.isNullOrBlank() && content.isNullOrBlank()) return null

        return when (NotificationType.fromString(payload.optNullableString("type"))) {
            NotificationType.MESSAGE -> AppNotification.Message(
                senderName = title.orEmpty(),
                body = content.orEmpty(),
                roomId = referenceId
            )
            NotificationType.FRIEND_REQUEST -> AppNotification.FriendRequest(title, content, referenceId)
            NotificationType.PLATE_REVIEW -> AppNotification.PlateReview(title, content, referenceId)
            NotificationType.NEW_FOLLOWER -> AppNotification.NewFollower(title, content, referenceId)
            NotificationType.SYSTEM -> AppNotification.System(title, content)
        }
    }

    private fun JSONObject.optNullableString(name: String): String? =
        if (has(name) && !isNull(name)) optString(name) else null

    private fun JSONObject.optNullableLong(name: String): Long? =
        if (has(name) && !isNull(name)) optLong(name) else null
}
