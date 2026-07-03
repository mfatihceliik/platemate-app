package com.mefy.platemate.core.notification.presenter

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mefy.platemate.R
import com.mefy.platemate.core.notification.channel.NotificationChannelRegistry
import com.mefy.platemate.domain.model.notification.NotificationType

/**
 * Tipe özgü presenter'ların ortak iskeleti. Alt sınıflar yalnızca kendi başlık/metin eşlemesini
 * verip [buildStandard]'ı çağırır; kanal, ikon, genişletilebilir stil ve tıklama hedefi
 * ([contentIntent]) burada standarttır.
 */
abstract class BaseNotificationPresenter(
    private val context: Context,
    private val channelRegistry: NotificationChannelRegistry
) : NotificationPresenter {

    protected fun buildStandard(
        type: NotificationType,
        title: String?,
        content: String?,
        contentIntent: PendingIntent
    ): Notification =
        NotificationCompat.Builder(context, channelRegistry.channelId(type))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title.orEmpty())
            .setContentText(content.orEmpty())
            .setStyle(NotificationCompat.BigTextStyle().bigText(content.orEmpty()))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
}
