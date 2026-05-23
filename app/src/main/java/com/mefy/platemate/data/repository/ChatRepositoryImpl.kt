package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.ChatMessageMapper
import com.mefy.platemate.data.mapper.ChatRoomMapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.data.remote.rest.service.ChatApiService
import com.mefy.platemate.data.remote.dto.chat.SendMessageRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeMessageCall
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApiService,
    private val socketMessagingDataSource: SocketMessagingDataSource,
    private val chatRoomMapper: ChatRoomMapper,
    private val chatMessageMapper: ChatMessageMapper,
    private val appDispatchers: AppDispatchers
) : ChatRepository {

    override suspend fun getMyChatRooms(): AppResult<List<ChatRoom>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getMyChatRooms() }.map(chatRoomMapper::mapList)
        }

    override suspend fun createOrGetPrivateChatRoom(otherUserId: Long): AppResult<ChatRoom> =
        withContext(appDispatchers.io) {
            safeApiCall { api.createOrGetPrivateChatRoom(otherUserId) }.map(chatRoomMapper::map)
        }

    override suspend fun getRoomMessages(roomId: Long): AppResult<List<ChatMessage>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getRoomMessages(roomId) }.map(chatMessageMapper::mapList)
        }

    override suspend fun markMessagesAsRead(roomId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.markMessagesAsRead(roomId) }
        }

    override suspend fun sendMessage(chatRoomId: Long, content: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.sendMessage(SendMessageRequest(chatRoomId, content)) }.map { Unit }
        }

    override fun observeMessages(roomId: Long): Flow<ChatMessage> =
        socketMessagingDataSource.observeMessages()
            .filter { message ->
                message.chatRoomId == null || message.chatRoomId == roomId
            }
            .flowOn(appDispatchers.io)
}


