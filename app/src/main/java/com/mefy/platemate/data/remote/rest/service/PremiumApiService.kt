package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.premium.PremiumCatalogDto
import retrofit2.http.GET

interface PremiumApiService {
    @GET("api/premium")
    suspend fun getCatalog(): DataResultResponse<PremiumCatalogDto>
}
