package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.user.UserProfileDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApiService {
    @GET("api/profiles/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: Long
    ): DataResultResponse<UserProfileDto>
}
