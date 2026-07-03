package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.user.UpdateProfileRequest
import com.mefy.platemate.data.remote.dto.user.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApiService {
    @GET("api/profiles/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: Long
    ): DataResultResponse<UserProfileDto>

    @PUT("api/profiles/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Long,
        @Body request: UpdateProfileRequest
    ): ResultResponse
}
