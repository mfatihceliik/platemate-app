package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.DiscoveryHomeResponseDto
import retrofit2.http.GET

interface DiscoveryApiService {
    @GET("api/discovery/home")
    suspend fun getDiscoveryHome(): DataResultResponse<DiscoveryHomeResponseDto>
}
