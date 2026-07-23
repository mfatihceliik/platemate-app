package com.mefy.platemate.presentation.common.global

import com.mefy.platemate.core.notification.model.AppNotification
import kotlinx.coroutines.flow.StateFlow

interface NotificationNavigationBus {
    val target: StateFlow<AppNotification?>
    fun post(notification: AppNotification)
    fun consume()
}
