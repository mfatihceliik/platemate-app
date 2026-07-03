package com.mefy.platemate.presentation.common.global

import com.mefy.platemate.core.notification.model.AppNotification
import kotlinx.coroutines.flow.StateFlow

/**
 * Sistem bildirimine dokunulduğunda [com.mefy.platemate.MainActivity]'nin çözdüğü hedefi kök
 * composable'a taşıyan süreç-geneli kanal. [StateFlow] olduğu için cold-start'ta hedef, ana grafik
 * hazır olana dek beklemede kalır; root tüketince [consume] ile temizlenir.
 */
interface NotificationNavigationBus {
    val target: StateFlow<AppNotification?>
    fun post(notification: AppNotification)
    fun consume()
}
