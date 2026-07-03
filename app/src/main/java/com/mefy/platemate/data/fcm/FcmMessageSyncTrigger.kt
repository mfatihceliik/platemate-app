package com.mefy.platemate.data.fcm

import com.mefy.platemate.core.coroutine.ApplicationScope
import com.mefy.platemate.core.notification.model.AppNotification
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.usecase.chat.SyncChatRoomsUseCase
import com.mefy.platemate.domain.usecase.chat.SyncRoomMessagesUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Data-only FCM arka planda [PlateMateFirebaseMessagingService.onMessageReceived]'i tetikler;
 * socket askıdayken kaçan veriyi REST'ten Room'a yazar. Böylece kullanıcı app'i açtığında sohbet
 * listesi (ve mesaj geldiyse o oda) zaten güncel olur. Yalnızca MESSAGE tipinde senkronlar.
 */
@Singleton
class FcmMessageSyncTrigger @Inject constructor(
    private val syncChatRoomsUseCase: SyncChatRoomsUseCase,
    private val syncRoomMessagesUseCase: SyncRoomMessagesUseCase,
    private val sessionStore: SessionStore,
    @param:ApplicationScope private val scope: CoroutineScope
) {
    fun onIncoming(notification: AppNotification) {
        if (notification !is AppNotification.Message) return
        scope.launch {
            if (sessionStore.peekToken().isNullOrBlank()) return@launch
            syncChatRoomsUseCase()
            notification.roomId?.let { roomId -> syncRoomMessagesUseCase(roomId) }
        }
    }
}
