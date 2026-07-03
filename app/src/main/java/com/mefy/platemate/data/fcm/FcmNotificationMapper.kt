package com.mefy.platemate.data.fcm

import com.google.firebase.messaging.RemoteMessage
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.domain.model.notification.NotificationType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * FCM [RemoteMessage]'ı tip-bazlı [AppNotification]'a çevirir. Tip [NotificationType.fromString]
 * ile çözülür; tanınmayan tip SYSTEM'e düşer. Gösterilecek içerik yoksa null döner.
 */
@Singleton
class FcmNotificationMapper @Inject constructor() {

    fun map(message: RemoteMessage): AppNotification? {
        val data = message.data
        val title = message.notification?.title ?: data["title"]
        val content = message.notification?.body ?: data["content"] ?: data["body"]
        val referenceId = data["referenceId"]?.toLongOrNull()

        if (title.isNullOrBlank() && content.isNullOrBlank()) return null

        return when (NotificationType.fromString(data["type"])) {
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
}
