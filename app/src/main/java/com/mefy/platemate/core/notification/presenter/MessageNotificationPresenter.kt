package com.mefy.platemate.core.notification.presenter

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import com.mefy.platemate.core.notification.channel.NotificationChannelRegistry
import com.mefy.platemate.core.notification.model.AppNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Sohbet mesajı: başlık gönderenin adı, metin genişletilebilir mesaj içeriği. */
@Singleton
class MessageNotificationPresenter @Inject constructor(
    @param:ApplicationContext context: Context,
    channelRegistry: NotificationChannelRegistry
) : BaseNotificationPresenter(context, channelRegistry) {

    override fun build(notification: AppNotification, contentIntent: PendingIntent): Notification {
        val message = notification as AppNotification.Message
        return buildStandard(message.type, message.senderName, message.body, contentIntent)
    }
}
