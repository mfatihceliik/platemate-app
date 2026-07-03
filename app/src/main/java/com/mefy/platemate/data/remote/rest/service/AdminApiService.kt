package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.admin.AdminCommentModerationRequest
import com.mefy.platemate.data.remote.dto.admin.AdminReviewRequest
import com.mefy.platemate.data.remote.dto.admin.AppSettingsDto
import com.mefy.platemate.data.remote.dto.admin.CommentReportDto
import com.mefy.platemate.data.remote.dto.admin.HidePlateRequest
import com.mefy.platemate.data.remote.dto.admin.PlateAdminDto
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalRequestDto
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeAdminDto
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeRequest
import com.mefy.platemate.data.remote.dto.admin.PlateReviewAdminDto
import com.mefy.platemate.data.remote.dto.admin.SocialPlatformAdminDto
import com.mefy.platemate.data.remote.dto.admin.SocialPlatformRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateAppSettingsRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateReportTypeActiveRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateSocialPlatformActiveRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminApiService {
    // --- Pending comments ---
    @GET("api/admin/comments/pending")
    suspend fun getPendingComments(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): DataResultResponse<PagedResult<PlateReviewAdminDto>>

    @PATCH("api/admin/comments/{commentId}/approve")
    suspend fun approveComment(@Path("commentId") commentId: Long): ResultResponse

    @PATCH("api/admin/comments/{commentId}/reject")
    suspend fun rejectComment(
        @Path("commentId") commentId: Long,
        @Body request: AdminCommentModerationRequest
    ): ResultResponse

    @PATCH("api/admin/comments/{commentId}/remove")
    suspend fun removeComment(
        @Path("commentId") commentId: Long,
        @Body request: AdminCommentModerationRequest
    ): ResultResponse

    // --- Comment reports ---
    @GET("api/admin/comment-reports")
    suspend fun getCommentReports(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): DataResultResponse<PagedResult<CommentReportDto>>

    @PATCH("api/admin/comment-reports/{reportId}/review")
    suspend fun reviewCommentReport(
        @Path("reportId") reportId: Long,
        @Body request: AdminReviewRequest
    ): ResultResponse

    // --- Plate removal requests ---
    @GET("api/admin/plate-removal-requests")
    suspend fun getPlateRemovalRequests(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): DataResultResponse<PagedResult<PlateRemovalRequestDto>>

    @PATCH("api/admin/plate-removal-requests/{requestId}/review")
    suspend fun reviewPlateRemovalRequest(
        @Path("requestId") requestId: Long,
        @Body request: AdminReviewRequest
    ): ResultResponse

    // --- Hidden plates ---
    @GET("api/admin/plates/hidden")
    suspend fun getHiddenPlates(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): DataResultResponse<PagedResult<PlateAdminDto>>

    @PATCH("api/admin/plates/{plateId}/restore")
    suspend fun restorePlate(@Path("plateId") plateId: Long): ResultResponse

    @PATCH("api/admin/plates/{plateId}/hide")
    suspend fun hidePlate(
        @Path("plateId") plateId: Long,
        @Body request: HidePlateRequest
    ): ResultResponse

    // --- App settings (tunable limits) ---
    @GET("api/admin/settings")
    suspend fun getAppSettings(): DataResultResponse<AppSettingsDto>

    @PUT("api/admin/settings")
    suspend fun updateAppSettings(@Body request: UpdateAppSettingsRequest): ResultResponse

    // --- Plate report types (CRUD) ---
    @GET("api/admin/plate-report-types")
    suspend fun getReportTypes(): DataResultResponse<List<PlateReportTypeAdminDto>>

    @POST("api/admin/plate-report-types")
    suspend fun addReportType(@Body request: PlateReportTypeRequest): DataResultResponse<PlateReportTypeAdminDto>

    @PUT("api/admin/plate-report-types/{id}")
    suspend fun updateReportType(
        @Path("id") id: Long,
        @Body request: PlateReportTypeRequest
    ): DataResultResponse<PlateReportTypeAdminDto>

    @PATCH("api/admin/plate-report-types/{id}/active")
    suspend fun setReportTypeActive(
        @Path("id") id: Long,
        @Body request: UpdateReportTypeActiveRequest
    ): ResultResponse

    // --- Social platforms (CRUD) ---
    @GET("api/admin/social-platforms")
    suspend fun getSocialPlatformsAdmin(): DataResultResponse<List<SocialPlatformAdminDto>>

    @POST("api/admin/social-platforms")
    suspend fun addSocialPlatform(@Body request: SocialPlatformRequest): DataResultResponse<SocialPlatformAdminDto>

    @PUT("api/admin/social-platforms/{id}")
    suspend fun updateSocialPlatform(
        @Path("id") id: Long,
        @Body request: SocialPlatformRequest
    ): DataResultResponse<SocialPlatformAdminDto>

    @PATCH("api/admin/social-platforms/{id}/active")
    suspend fun setSocialPlatformActive(
        @Path("id") id: Long,
        @Body request: UpdateSocialPlatformActiveRequest
    ): ResultResponse
}
