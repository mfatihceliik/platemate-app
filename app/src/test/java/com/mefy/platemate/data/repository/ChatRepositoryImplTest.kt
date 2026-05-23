package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.ChatMessageMapper
import com.mefy.platemate.data.mapper.ChatRoomMapper
import com.mefy.platemate.data.remote.dto.chat.ChatMessageDto
import com.mefy.platemate.data.remote.dto.chat.ChatRoomDto
import com.mefy.platemate.data.remote.dto.chat.SendMessageRequest
import com.mefy.platemate.data.remote.rest.service.ChatApiService
import com.mefy.platemate.data.remote.websocket.datasource.SocketMessagingDataSource
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun observeMessages_filtersByRoomId_butKeepsNullRoomMessages() = runTest {
        val messages = MutableSharedFlow<ChatMessage>()
        val repository = ChatRepositoryImpl(
            api = FakeChatApiService(),
            socketMessagingDataSource = object : SocketMessagingDataSource {
                override suspend fun sendMessage(chatRoomId: Long, content: String) = Unit
                override fun observeMessages(): Flow<ChatMessage> = messages
            },
            chatRoomMapper = ChatRoomMapper(),
            chatMessageMapper = ChatMessageMapper(),
            appDispatchers = testDispatchers()
        )

        val firstEmission = async { repository.observeMessages(roomId = 42L).first() }

        messages.emit(
            ChatMessage(
                id = 1L,
                chatRoomId = 99L,
                senderUsername = "fatih",
                content = "ignore me",
                sentAt = null,
                isRead = false
            )
        )
        runCurrent()
        assertFalse(firstEmission.isCompleted)

        val expected = ChatMessage(
            id = 2L,
            chatRoomId = null,
            senderUsername = "system",
            content = "broadcast",
            sentAt = null,
            isRead = true
        )
        messages.emit(expected)

        assertEquals(expected, firstEmission.await())
    }

    private fun testDispatchers(): AppDispatchers = AppDispatchers(
        main = mainDispatcherRule.dispatcher,
        io = mainDispatcherRule.dispatcher,
        default = mainDispatcherRule.dispatcher
    )

    private class FakeChatApiService : ChatApiService {
        override suspend fun getMyChatRooms(): DataResultResponse<List<ChatRoomDto>> =
            DataResultResponse(success = true, message = null, data = emptyList())

        override suspend fun createOrGetPrivateChatRoom(otherUserId: Long): DataResultResponse<ChatRoomDto> =
            DataResultResponse(success = true, message = null, data = null)

        override suspend fun getRoomMessages(roomId: Long): DataResultResponse<List<ChatMessageDto>> =
            DataResultResponse(success = true, message = null, data = emptyList())

        override suspend fun markMessagesAsRead(roomId: Long): ResultResponse =
            ResultResponse(success = true, message = null)

        override suspend fun sendMessage(request: SendMessageRequest): DataResultResponse<ChatMessageDto> =
            DataResultResponse(success = true, message = null, data = null)
    }
}
