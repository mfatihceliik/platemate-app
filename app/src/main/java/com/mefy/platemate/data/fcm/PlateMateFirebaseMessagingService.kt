package com.mefy.platemate.data.fcm

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mefy.platemate.core.notification.AppNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * İnce FCM giriş noktası: token kaydını [FcmTokenRegistrar]'a, mesaj çözümlemesini
 * [FcmNotificationMapper]'a, gösterimi [AppNotificationManager]'a delege eder.
 */
@AndroidEntryPoint
class PlateMateFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var notificationMapper: FcmNotificationMapper
    @Inject lateinit var appNotificationManager: AppNotificationManager
    @Inject lateinit var tokenRegistrar: FcmTokenRegistrar
    @Inject lateinit var messageSyncTrigger: FcmMessageSyncTrigger

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        tokenRegistrar.onNewToken(token)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = notificationMapper.map(message) ?: return
        // Bildirimi göster (foreground'da AppNotificationManager bastırır; arka planda gösterir).
        appNotificationManager.show(notification)
        // Arka planda socket askıdayken kaçan veriyi Room'a çek.
        messageSyncTrigger.onIncoming(notification)
    }
}
