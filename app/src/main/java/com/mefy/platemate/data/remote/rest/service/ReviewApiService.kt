package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.data.remote.dto.plate.PlateReviewDto
import com.mefy.platemate.data.remote.dto.plate.AddPlateReviewRequest
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.report.PlateReportTypeDto
import com.mefy.platemate.data.remote.dto.review.ReviewResponseDto
import com.mefy.platemate.data.remote.dto.review.UpdatePlateReviewRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApiService {
    @POST("api/plates/{plateCode}/reviews")
    suspend fun addOrUpdateMyReviewForPlate(
        @Path("plateCode") plateCode: String,
        @Body request: AddPlateReviewRequest
    ): DataResultResponse<ReviewResponseDto>

    @GET("api/plates/{plateCode}/reviews")
    suspend fun getPlateReviews(
        @Path("plateCode") plateCode: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): DataResultResponse<PagedResult<PlateReviewDto>>

    @GET("api/plate-report-types")
    suspend fun getReportTypes(): DataResultResponse<List<PlateReportTypeDto>>

    @PUT("api/plates/reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Long,
        @Body request: UpdatePlateReviewRequest
    ): ResultResponse

    @DELETE("api/plates/reviews/{id}")
    suspend fun deleteReview(@Path("id") id: Long): ResultResponse
}

