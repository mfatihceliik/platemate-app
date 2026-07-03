package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalRequestDto
import com.mefy.platemate.data.remote.dto.plate.AddPlateRemovalRequestRequest
import com.mefy.platemate.data.remote.dto.plate.MyPlateListsDto
import com.mefy.platemate.data.remote.dto.plate.PlateSearchResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlateApiService {
    @GET("api/plates/search")
    suspend fun searchPlate(@Query("plate") plate: String): DataResultResponse<PlateSearchResponseDto>

    @POST("api/plates/{plateCode}/follow")
    suspend fun followPlate(@Path("plateCode") plateCode: String): ResultResponse

    @DELETE("api/plates/{plateCode}/follow")
    suspend fun unfollowPlate(@Path("plateCode") plateCode: String): ResultResponse

    @GET("api/plates/my-lists")
    suspend fun getMyLists(): DataResultResponse<MyPlateListsDto>

    @POST("api/plates/{plateCode}/save")
    suspend fun savePlate(@Path("plateCode") plateCode: String): ResultResponse

    @DELETE("api/plates/{plateCode}/save")
    suspend fun unsavePlate(@Path("plateCode") plateCode: String): ResultResponse

    @POST("api/plates/{plateCode}/alarm")
    suspend fun createAlarm(@Path("plateCode") plateCode: String): ResultResponse

    @DELETE("api/plates/{plateCode}/alarm")
    suspend fun removeAlarm(@Path("plateCode") plateCode: String): ResultResponse

    @POST("api/plates/{plateId}/removal-requests")
    suspend fun createRemovalRequest(
        @Path("plateId") plateId: Long,
        @Body request: AddPlateRemovalRequestRequest
    ): DataResultResponse<PlateRemovalRequestDto>
}
