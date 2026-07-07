package com.mefy.platemate.core.notification.presenter

import android.app.Notification
import android.app.PendingIntent
import com.mefy.platemate.core.notification.model.AppNotification

/** Tek bir bildirim tipini Android [Notification] nesnesine dönüştürür. */
interface NotificationPresenter {
    /** [contentIntent] tıklamada açılacak hedefi taşır (deeplink → sohbet/ekran). */
    fun build(notification: AppNotification, contentIntent: PendingIntent): Notification
}
