package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.theme.ThemeCatalogDto
import retrofit2.http.GET

interface ThemeApiService {
    @GET("api/theme/catalog")
    suspend fun getCatalog(): DataResultResponse<ThemeCatalogDto>
}
