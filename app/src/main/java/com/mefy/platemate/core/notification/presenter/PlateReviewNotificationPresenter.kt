package com.mefy.platemate.core.notification.presenter

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import com.mefy.platemate.core.notification.channel.NotificationChannelRegistry
import com.mefy.platemate.core.notification.model.AppNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Plaka değerlendirmesi bildirimi. */
@Singleton
class PlateReviewNotificationPresenter @Inject constructor(
    @ApplicationContext context: Context,
    channelRegistry: NotificationChannelRegistry
) : BaseNotificationPresenter(context, channelRegistry) {

    override fun build(notification: AppNotification, contentIntent: PendingIntent): Notification {
        val plateReview = notification as AppNotification.PlateReview
        return buildStandard(plateReview.type, plateReview.title, plateReview.content, contentIntent)
    }
}
