package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.DiscoveryHomeResponseDto
import com.mefy.platemate.data.remote.dto.city.CityPlateActivityDto
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscoveryApiService {
    @GET("api/discovery/home")
    suspend fun getDiscoveryHome(): DataResultResponse<DiscoveryHomeResponseDto>

    @GET("api/discovery/cities/{cityId}/plates")
    suspend fun getCityPlates(
        @Path("cityId") cityId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): DataResultResponse<PagedResult<CityPlateActivityDto>>

    @GET("api/discovery/tabs/{tabType}/plates")
    suspend fun getDiscoveryTabPlates(
        @Path("tabType") tabType: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("cityIds") cityIds: List<Int>?,
        @Query("reportTypeCode") reportTypeCode: String?,
        @Query("minRating") minRating: Double?,
        @Query("windowDays") windowDays: Int?
    ): DataResultResponse<PagedResult<PlateDetailDto>>
}
