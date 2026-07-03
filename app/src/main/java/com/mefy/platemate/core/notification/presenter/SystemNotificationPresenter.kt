package com.mefy.platemate.core.notification.presenter

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import com.mefy.platemate.core.notification.channel.NotificationChannelRegistry
import com.mefy.platemate.core.notification.model.AppNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Genel/sistem bildirimi (varsayılan tip). */
@Singleton
class SystemNotificationPresenter @Inject constructor(
    @param:ApplicationContext context: Context,
    channelRegistry: NotificationChannelRegistry
) : BaseNotificationPresenter(context, channelRegistry) {

    override fun build(notification: AppNotification, contentIntent: PendingIntent): Notification {
        val system = notification as AppNotification.System
        return buildStandard(system.type, system.title, system.content, contentIntent)
    }
}
