package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.settings.SettingsOverviewDto
import com.mefy.platemate.data.remote.dto.settings.UpdateAppearanceRequest
import com.mefy.platemate.data.remote.dto.settings.UpdateSettingsRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT

interface SettingsApiService {
    @GET("api/settings/{userId}/overview")
    suspend fun getSettingsOverview(
        @Path("userId") userId: Long
    ): DataResultResponse<SettingsOverviewDto>

    @PUT("api/settings/{userId}")
    suspend fun updateSettings(
        @Path("userId") userId: Long,
        @Body request: UpdateSettingsRequest
    ): ResultResponse

    @PUT("api/settings/{userId}/appearance")
    suspend fun updateAppearance(
        @Path("userId") userId: Long,
        @Body request: UpdateAppearanceRequest
    ): ResultResponse
}
