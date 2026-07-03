package com.mefy.platemate.presentation.common.global

import com.mefy.platemate.core.notification.model.AppNotification
import kotlinx.coroutines.flow.SharedFlow

/**
 * Uygulama AÇIKKEN (foreground) gelen bildirimleri tek noktadan banner'a ([com.mefy.platemate
 * .presentation.components.PMInAppNotificationBanner]) taşıyan süreç-geneli kanal. Yayıncı
 * [com.mefy.platemate.core.startup.InAppNotificationCoordinator], tüketici kök composable.
 * [GlobalUiEventBus] ile aynı desen; ayrı kanal çünkü farklı bir UX (üstten banner) sürüyor.
 */
interface InAppNotificationBus {

    val events: SharedFlow<AppNotification>

    fun emit(notification: AppNotification)
}
