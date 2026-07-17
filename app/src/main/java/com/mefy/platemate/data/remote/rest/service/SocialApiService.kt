package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.friend.FriendshipDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SocialApiService {
    @POST("api/friendships/request/{addresseeId}")
    suspend fun sendFriendRequest(@Path("addresseeId") addresseeId: Long): ResultResponse

    @PUT("api/friendships/{id}/accept")
    suspend fun acceptFriendRequest(@Path("id") id: Long): ResultResponse

    @PUT("api/friendships/{id}/reject")
    suspend fun rejectFriendRequest(@Path("id") id: Long): ResultResponse

    @DELETE("api/friendships/{id}")
    suspend fun removeFriend(@Path("id") id: Long): ResultResponse

    @GET("api/friendships")
    suspend fun getMyFriends(): DataResultResponse<List<FriendshipDto>>

    @GET("api/friendships/pending")
    suspend fun getPendingRequests(): DataResultResponse<List<FriendshipDto>>

    @GET("api/friendships/pending/sent")
    suspend fun getSentPendingRequests(): DataResultResponse<List<FriendshipDto>>
}

