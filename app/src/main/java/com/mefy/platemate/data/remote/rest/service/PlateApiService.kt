package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.plate.PlateSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PlateApiService {
    @GET("api/plates/search")
    suspend fun searchPlate(@Query("plate") plate: String): DataResultResponse<PlateSearchResponseDto>
}
