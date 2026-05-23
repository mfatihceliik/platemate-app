package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.plate.PlateDto
import com.mefy.platemate.data.remote.dto.plate.PlateSearchRequest
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlateApiService {
    @GET("api/plates/search")
    suspend fun searchPlate(@Query("plate") plate: String): DataResultResponse<PlateSearchRequest>

    @GET("api/plates/search/{plate}")
    suspend fun searchPlateByPath(@Path("plate") plate: String): DataResultResponse<PlateDto>
}
