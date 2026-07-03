package com.mefy.platemate.data.remote.websocket

import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

/**
 * Uygulama-geneli canlı akışı (tüm odalar, filtresiz) dinleyip Room'a yazan köprü. UI artık
 * Room'u dinlediği için, kullanıcı hangi ekranda olursa olsun gelen mesaj/durum buradan kalıcılaşır.
 * Yaşam döngüsü [com.mefy.platemate.MainActivityViewModel] tarafından oturuma bağlı yönetilir.
 */
interface ChatLiveSync {
    fun updates(): Flow<Unit>
}

@Singleton
class ChatLiveSyncImpl @Inject constructor(
    private val socketMessagingDataSource: SocketMessagingDataSource,
    private val chatRepository: ChatRepository
) : ChatLiveSync {

    override fun updates(): Flow<Unit> = merge(
        socketMessagingDataSource.observeMessages()
            .onEach { chatRepository.cacheIncomingMessage(it) }
            .map { },
        socketMessagingDataSource.observeMessageDelivered()
            .onEach { chatRepository.cacheMessageStatus(it) }
            .map { },
        socketMessagingDataSource.observeMessageRead()
            .onEach { chatRepository.cacheMessageStatus(it) }
            .map { }
    )
}
