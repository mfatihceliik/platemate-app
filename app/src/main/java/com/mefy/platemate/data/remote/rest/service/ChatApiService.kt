package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.chat.ChatMessageDto
import com.mefy.platemate.data.remote.dto.chat.ChatRoomDto
import com.mefy.platemate.data.remote.dto.chat.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {
    @GET("api/chat/rooms")
    suspend fun getMyChatRooms(): DataResultResponse<List<ChatRoomDto>>

    @POST("api/chat/rooms")
    suspend fun createOrGetPrivateChatRoom(@Query("otherUserId") otherUserId: Long): DataResultResponse<ChatRoomDto>

    @GET("api/chat/rooms/{roomId}/messages")
    suspend fun getRoomMessages(@Path("roomId") roomId: Long): DataResultResponse<List<ChatMessageDto>>

    @PUT("api/chat/rooms/{roomId}/read")
    suspend fun markMessagesAsRead(@Path("roomId") roomId: Long): ResultResponse

    @POST("api/chat/rooms/messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): DataResultResponse<ChatMessageDto>
}

