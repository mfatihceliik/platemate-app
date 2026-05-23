package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.user.UserLocationDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LocationApiService {
    @GET("api/locations/user/{userId}")
    suspend fun getUserLocation(@Path("userId") userId: Long): DataResultResponse<UserLocationDto>

    @GET("api/locations/visible")
    suspend fun getVisibleLocations(): DataResultResponse<List<UserLocationDto>>

    @POST("api/locations/block/{targetUserId}")
    suspend fun blockUserFromLocation(@Path("targetUserId") targetUserId: Long): ResultResponse

    @DELETE("api/locations/block/{targetUserId}")
    suspend fun unblockUserFromLocation(@Path("targetUserId") targetUserId: Long): ResultResponse

    @GET("api/locations/blocked")
    suspend fun getBlockedLocationUsers(): DataResultResponse<List<Long>>
}

