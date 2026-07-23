package com.mefy.platemate.presentation.app.viewmodel

import androidx.lifecycle.viewModelScope
import com.mefy.platemate.core.connectivity.NetworkMonitor
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.presentation.common.global.GlobalAppEvent
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.global.InAppNotificationBus
import com.mefy.platemate.presentation.common.global.NotificationNavigationBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventBus: GlobalUiEventBus,
    inAppNotificationBus: InAppNotificationBus,
    private val notificationNavigationBus: NotificationNavigationBus
) : BaseViewModel(globalUiEventBus) {

    val globalUiEvents: SharedFlow<GlobalAppEvent> = globalUiEventBus.events
    val inAppNotifications: SharedFlow<AppNotification> = inAppNotificationBus.events
    val notificationNavTarget: StateFlow<AppNotification?> = notificationNavigationBus.target
    fun consumeNotificationNavTarget() = notificationNavigationBus.consume()

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
}
