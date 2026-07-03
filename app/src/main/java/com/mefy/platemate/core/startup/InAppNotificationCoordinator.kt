package com.mefy.platemate.core.startup

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.core.notification.ActiveConversationTracker
import com.mefy.platemate.core.notification.foreground.AppForegroundState
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.data.remote.websocket.datasource.SocketNotificationDataSource
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.presentation.common.global.InAppNotificationBus
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Oturum açıkken soket `notification_received` akışını dinler ve app **foreground** iken
 * uygulama-içi banner'a yönlendirir ([InAppNotificationBus]). Kullanıcı o sohbet odasındaysa o
 * odanın mesaj banner'ı bastırılır (içerik zaten ekranda). Foreground değilken banner yok — o
 * durumda FCM sistem bildirimi devrede.
 */
@Singleton
class InAppNotificationCoordinator @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val socketNotificationDataSource: SocketNotificationDataSource,
    private val inAppNotificationBus: InAppNotificationBus,
    private val foregroundState: AppForegroundState,
    private val activeConversationTracker: ActiveConversationTracker,
    @param:ApplicationScope private val scope: CoroutineScope
) : AppCoordinator {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun start() {
        scope.launch {
            observeSessionUseCase()
                .flatMapLatest { session ->
                    if (session == null) emptyFlow() else socketNotificationDataSource.observeNotifications()
                }
                .collect { notification ->
                    if (!foregroundState.isForeground) return@collect
                    if (notification is AppNotification.Message &&
                        notification.roomId != null &&
                        notification.roomId == activeConversationTracker.activeRoomId
                    ) {
                        return@collect
                    }
                    inAppNotificationBus.emit(notification)
                }
        }
    }
}
