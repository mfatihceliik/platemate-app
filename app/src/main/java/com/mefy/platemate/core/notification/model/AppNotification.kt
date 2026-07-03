package com.mefy.platemate.core.notification.model

import com.mefy.platemate.domain.model.notification.NotificationType

/**
 * Gösterilecek bir bildirimin tip-bazlı, kendine yeten temsili. Her alt tip yalnızca kendi
 * render'ı için gereken veriyi taşır; nasıl çizileceğini ilgili
 * [com.mefy.platemate.core.notification.presenter.NotificationPresenter] bilir.
 */
sealed interface AppNotification {

    val type: NotificationType

    /** Kısa süre içinde yinelenen (örn. FCM+socket) bildirimleri elemek için kullanılan anahtar. */
    val dedupeKey: String

    /** Sohbet mesajı: başlık gönderenin adı, metin mesaj içeriği (genişletilebilir). */
    data class Message(
        val senderName: String,
        val body: String,
        val roomId: Long?
    ) : AppNotification {
        override val type get() = NotificationType.MESSAGE
        override val dedupeKey get() = "MESSAGE|$roomId|$body"
    }

    data class FriendRequest(
        val title: String?,
        val content: String?,
        val referenceId: Long?
    ) : AppNotification {
        override val type get() = NotificationType.FRIEND_REQUEST
        override val dedupeKey get() = "FRIEND_REQUEST|${title.orEmpty()}|${content.orEmpty()}"
    }

    data class PlateReview(
        val title: String?,
        val content: String?,
        val referenceId: Long?
    ) : AppNotification {
        override val type get() = NotificationType.PLATE_REVIEW
        override val dedupeKey get() = "PLATE_REVIEW|${title.orEmpty()}|${content.orEmpty()}"
    }

    data class NewFollower(
        val title: String?,
        val content: String?,
        val referenceId: Long?
    ) : AppNotification {
        override val type get() = NotificationType.NEW_FOLLOWER
        override val dedupeKey get() = "NEW_FOLLOWER|${title.orEmpty()}|${content.orEmpty()}"
    }

    data class System(
        val title: String?,
        val content: String?
    ) : AppNotification {
        override val type get() = NotificationType.SYSTEM
        override val dedupeKey get() = "SYSTEM|${title.orEmpty()}|${content.orEmpty()}"
    }
}
