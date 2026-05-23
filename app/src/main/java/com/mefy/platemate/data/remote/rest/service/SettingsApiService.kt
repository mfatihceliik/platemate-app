package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.user.UserSettingsDto
import com.mefy.platemate.data.remote.dto.settings.UpdateSettingsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT

interface SettingsApiService {
    @GET("api/settings/{userId}")
    suspend fun getMySettings(@Path("userId") userId: Long): DataResultResponse<UserSettingsDto>

    @PUT("api/settings/{userId}")
    suspend fun updateSettings(
        @Path("userId") userId: Long,
        @Body request: UpdateSettingsRequest
    ): ResultResponse
}

