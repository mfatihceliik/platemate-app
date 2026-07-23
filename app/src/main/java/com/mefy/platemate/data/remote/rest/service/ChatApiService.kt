package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.chat.AddChatMessageReportRequest
import com.mefy.platemate.data.remote.dto.chat.ChatMessageDto
import com.mefy.platemate.data.remote.dto.chat.ChatRoomDto
import com.mefy.platemate.data.remote.dto.chat.PresenceDto
import com.mefy.platemate.data.remote.dto.chat.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @GET("api/chat/rooms/{roomId}")
    suspend fun getRoom(@Path("roomId") roomId: Long): DataResultResponse<ChatRoomDto>

    @GET("api/chat/rooms/{roomId}/messages")
    suspend fun getRoomMessages(@Path("roomId") roomId: Long): DataResultResponse<List<ChatMessageDto>>

    @GET("api/chat/rooms/{roomId}/presence")
    suspend fun getRoomPresence(@Path("roomId") roomId: Long): DataResultResponse<PresenceDto>

    @PUT("api/chat/rooms/{roomId}/read")
    suspend fun markMessagesAsRead(@Path("roomId") roomId: Long): ResultResponse

    @POST("api/chat/rooms/messages")
    suspend fun sendMessage(@Body request: SendMessageRequest): DataResultResponse<ChatMessageDto>

    @POST("api/chat/rooms/{roomId}/accept")
    suspend fun acceptChatRequest(@Path("roomId") roomId: Long): ResultResponse

    @POST("api/chat/rooms/{roomId}/decline")
    suspend fun declineChatRequest(@Path("roomId") roomId: Long): ResultResponse

    @DELETE("api/chat/rooms/{roomId}")
    suspend fun leaveRoom(@Path("roomId") roomId: Long): ResultResponse

    @DELETE("api/chat/messages/{messageId}")
    suspend fun deleteMessage(@Path("messageId") messageId: Long): ResultResponse

    @POST("api/chat/messages/{messageId}/reports")
    suspend fun reportMessage(
        @Path("messageId") messageId: Long,
        @Body request: AddChatMessageReportRequest
    ): ResultResponse
}

