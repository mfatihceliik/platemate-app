package com.mefy.platemate.presentation.common.global

import com.mefy.platemate.core.notification.model.AppNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultNotificationNavigationBus @Inject constructor() : NotificationNavigationBus {

    private val _target = MutableStateFlow<AppNotification?>(null)
    override val target: StateFlow<AppNotification?> = _target.asStateFlow()

    override fun post(notification: AppNotification) {
        _target.value = notification
    }

    override fun consume() {
        _target.value = null
    }
}
