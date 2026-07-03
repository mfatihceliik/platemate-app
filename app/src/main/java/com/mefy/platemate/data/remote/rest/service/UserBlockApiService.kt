package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.ResultResponse
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface UserBlockApiService {
    @POST("api/users/{userId}/block")
    suspend fun blockUser(@Path("userId") userId: Long): ResultResponse

    @DELETE("api/users/{userId}/block")
    suspend fun unblockUser(@Path("userId") userId: Long): ResultResponse
}
