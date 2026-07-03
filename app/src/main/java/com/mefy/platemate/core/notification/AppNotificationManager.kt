package com.mefy.platemate.core.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.mefy.platemate.core.notification.foreground.AppForegroundState
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.core.notification.presenter.NotificationPresenter
import com.mefy.platemate.domain.model.notification.NotificationType
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bildirim gösterim koordinatörü: izin → foreground → aktif-oda → dedupe kapılarından geçen
 * [AppNotification]'ı tipine karşılık gelen [NotificationPresenter]'a çizdirir. Render bilgisi
 * presenter'larda; bu sınıf yalnızca "gösterilsin mi" kararını ve dağıtımı yönetir.
 */
@Singleton
class AppNotificationManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val presenters: Map<NotificationType, @JvmSuppressWildcards NotificationPresenter>,
    private val activeConversationTracker: ActiveConversationTracker,
    private val foregroundState: AppForegroundState,
    private val intentFactory: NotificationIntentFactory
) {
    private val notificationId = AtomicInteger(1000)

    // Kısa pencerede yinelenen aynı içeriği bastır (örn. FCM + socket çakışması).
    @Volatile private var lastKey: String? = null
    @Volatile private var lastShownAt: Long = 0L

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun show(notification: AppNotification) {
        if (!hasPostPermission()) return

        // Yalnızca app arka planda/kapalıyken bildir; foreground'da canlı içerik zaten ekranda.
        if (foregroundState.isForeground) return

        // Kullanıcı o sohbet odasındaysa o odanın mesaj bildirimini bastır.
        if (notification is AppNotification.Message &&
            notification.roomId != null &&
            notification.roomId == activeConversationTracker.activeRoomId
        ) {
            return
        }

        if (isDuplicate(notification.dedupeKey)) return

        val presenter = presenters[notification.type] ?: return
        val built = presenter.build(notification, intentFactory.contentIntent(notification))
        NotificationManagerCompat.from(context)
            .notify(notificationId.incrementAndGet(), built)
    }

    private fun isDuplicate(key: String): Boolean {
        val now = System.currentTimeMillis()
        if (key == lastKey && now - lastShownAt < DEDUPE_WINDOW_MS) return true
        lastKey = key
        lastShownAt = now
        return false
    }

    private fun hasPostPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private companion object {
        const val DEDUPE_WINDOW_MS = 4000L
    }
}
