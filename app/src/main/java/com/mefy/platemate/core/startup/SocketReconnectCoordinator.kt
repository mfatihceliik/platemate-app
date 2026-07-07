package com.mefy.platemate.core.startup

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.core.notification.ActiveConversationTracker
import com.mefy.platemate.core.notification.foreground.AppForegroundState
import com.mefy.platemate.data.remote.websocket.datasource.SocketConnectionDataSource
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.chat.SyncChatRoomsUseCase
import com.mefy.platemate.domain.usecase.chat.SyncRoomMessagesUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * App foreground'a her dönüşte, oturum açıksa soketi yeniden doğrular (arka planda/ağ nedeniyle
 * düşmüş olabilir) ve REST'ten tazeler: sohbet listesi **ve** o an açık olan konuşmanın mesajları.
 * Böylece soket kopukken kaçan mesajlar hem listede hem açık sohbette yakalanır.
 * [SocketLifecycleCoordinator] (session→connect/disconnect) ile birlikte çalışır.
 */
@Singleton
class SocketReconnectCoordinator @Inject constructor(
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val socketConnectionDataSource: SocketConnectionDataSource,
    private val syncChatRoomsUseCase: SyncChatRoomsUseCase,
    private val syncRoomMessagesUseCase: SyncRoomMessagesUseCase,
    private val activeConversationTracker: ActiveConversationTracker,
    private val appForegroundState: AppForegroundState,
    @param:ApplicationScope private val scope: CoroutineScope
) : AppCoordinator {

    override fun start() {
        scope.launch {
            appForegroundState.foregroundTransitions.collect {
                if (observeSessionUseCase().first() == null) return@collect
                if (!socketConnectionDataSource.isConnected()) {
                    socketConnectionDataSource.connect()
                }
                syncChatRoomsUseCase()
                // Açık konuşma varsa onun mesajlarını da tazele (background'dayken kaçanlar).
                activeConversationTracker.activeRoomId?.let { roomId ->
                    syncRoomMessagesUseCase(roomId)
                }
            }
        }
    }
}
