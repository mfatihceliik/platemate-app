package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.ResultResponse
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface FollowApiService {
    @POST("api/follows/{userId}")
    suspend fun followUser(@Path("userId") userId: Long): ResultResponse

    @DELETE("api/follows/{userId}")
    suspend fun unfollowUser(@Path("userId") userId: Long): ResultResponse
}
