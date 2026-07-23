package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.follow.FollowListItemDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowApiService {
    @POST("api/follows/{userId}")
    suspend fun followUser(@Path("userId") userId: Long): ResultResponse

    @DELETE("api/follows/{userId}")
    suspend fun unfollowUser(@Path("userId") userId: Long): ResultResponse

    @GET("api/follows/{userId}/followers")
    suspend fun getFollowers(@Path("userId") userId: Long): DataResultResponse<List<FollowListItemDto>>

    @GET("api/follows/{userId}/following")
    suspend fun getFollowing(@Path("userId") userId: Long): DataResultResponse<List<FollowListItemDto>>
}
