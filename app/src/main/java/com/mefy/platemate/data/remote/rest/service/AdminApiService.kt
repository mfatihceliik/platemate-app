package com.mefy.platemate.data.remote.rest.service

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.DataResultResponse
import com.mefy.platemate.core.common.result.ResultResponse
import com.mefy.platemate.data.remote.dto.admin.AdminCommentModerationRequest
import com.mefy.platemate.data.remote.dto.admin.AdminMenuItemDto
import com.mefy.platemate.data.remote.dto.admin.AdminReviewRequest
import com.mefy.platemate.data.remote.dto.admin.AppSettingsDto
import com.mefy.platemate.data.remote.dto.admin.CommentReportDto
import com.mefy.platemate.data.remote.dto.admin.CommentReportReasonAdminDto
import com.mefy.platemate.data.remote.dto.admin.CommentReportReasonRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateCommentReportReasonActiveRequest
import com.mefy.platemate.data.remote.dto.admin.HidePlateRequest
import com.mefy.platemate.data.remote.dto.admin.PlateAdminDto
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalRequestDto
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeAdminDto
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeRequest
import com.mefy.platemate.data.remote.dto.admin.AccentColorAdminDto
import com.mefy.platemate.data.remote.dto.admin.AccentColorRequest
import com.mefy.platemate.data.remote.dto.admin.ThemeGridSizeRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateAccentColorActiveRequest
import com.mefy.platemate.data.remote.dto.admin.PlateReviewAdminDto
import com.mefy.platemate.data.remote.dto.admin.PremiumFeatureAdminDto
import com.mefy.platemate.data.remote.dto.admin.PremiumFeatureRequest
import com.mefy.platemate.data.remote.dto.admin.PremiumPlanAdminDto
import com.mefy.platemate.data.remote.dto.admin.PremiumPlanRequest
import com.mefy.platemate.data.remote.dto.admin.UpdatePremiumActiveRequest
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
    // --- Admin hub menu ---
    @GET("api/admin/menu")
    suspend fun getAdminMenu(): DataResultResponse<List<AdminMenuItemDto>>

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

    // --- Comment report reasons (CRUD) ---
    @GET("api/admin/comment-report-reasons")
    suspend fun getCommentReportReasons(): DataResultResponse<List<CommentReportReasonAdminDto>>

    @POST("api/admin/comment-report-reasons")
    suspend fun addCommentReportReason(@Body request: CommentReportReasonRequest): DataResultResponse<CommentReportReasonAdminDto>

    @PUT("api/admin/comment-report-reasons/{id}")
    suspend fun updateCommentReportReason(
        @Path("id") id: Long,
        @Body request: CommentReportReasonRequest
    ): DataResultResponse<CommentReportReasonAdminDto>

    @PATCH("api/admin/comment-report-reasons/{id}/active")
    suspend fun setCommentReportReasonActive(
        @Path("id") id: Long,
        @Body request: UpdateCommentReportReasonActiveRequest
    ): ResultResponse

    // --- Plate removal reasons (CRUD) ---
    @GET("api/admin/plate-removal-reasons")
    suspend fun getPlateRemovalReasons(): DataResultResponse<List<com.mefy.platemate.data.remote.dto.admin.PlateRemovalReasonAdminDto>>

    @POST("api/admin/plate-removal-reasons")
    suspend fun addPlateRemovalReason(@Body request: com.mefy.platemate.data.remote.dto.admin.PlateRemovalReasonRequest): DataResultResponse<com.mefy.platemate.data.remote.dto.admin.PlateRemovalReasonAdminDto>

    @PUT("api/admin/plate-removal-reasons/{id}")
    suspend fun updatePlateRemovalReason(
        @Path("id") id: Long,
        @Body request: com.mefy.platemate.data.remote.dto.admin.PlateRemovalReasonRequest
    ): DataResultResponse<com.mefy.platemate.data.remote.dto.admin.PlateRemovalReasonAdminDto>

    @PATCH("api/admin/plate-removal-reasons/{id}/active")
    suspend fun setPlateRemovalReasonActive(
        @Path("id") id: Long,
        @Body request: com.mefy.platemate.data.remote.dto.admin.UpdatePlateRemovalReasonActiveRequest
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

    // --- Premium plans (edit-only) ---
    @GET("api/admin/premium/plans")
    suspend fun getPremiumPlans(): DataResultResponse<List<PremiumPlanAdminDto>>

    @PUT("api/admin/premium/plans/{id}")
    suspend fun updatePremiumPlan(
        @Path("id") id: Long,
        @Body request: PremiumPlanRequest
    ): DataResultResponse<PremiumPlanAdminDto>

    @PATCH("api/admin/premium/plans/{id}/active")
    suspend fun setPremiumPlanActive(
        @Path("id") id: Long,
        @Body request: UpdatePremiumActiveRequest
    ): ResultResponse

    // --- Premium features (CRUD) ---
    @GET("api/admin/premium/features")
    suspend fun getPremiumFeatures(): DataResultResponse<List<PremiumFeatureAdminDto>>

    @POST("api/admin/premium/features")
    suspend fun addPremiumFeature(@Body request: PremiumFeatureRequest): DataResultResponse<PremiumFeatureAdminDto>

    @PUT("api/admin/premium/features/{id}")
    suspend fun updatePremiumFeature(
        @Path("id") id: Long,
        @Body request: PremiumFeatureRequest
    ): DataResultResponse<PremiumFeatureAdminDto>

    @PATCH("api/admin/premium/features/{id}/active")
    suspend fun setPremiumFeatureActive(
        @Path("id") id: Long,
        @Body request: UpdatePremiumActiveRequest
    ): ResultResponse

    // --- Theme accent colors (CRUD) + grid size ---
    @GET("api/admin/theme/colors")
    suspend fun getAccentColors(): DataResultResponse<List<AccentColorAdminDto>>

    @POST("api/admin/theme/colors")
    suspend fun addAccentColor(@Body request: AccentColorRequest): DataResultResponse<AccentColorAdminDto>

    @PUT("api/admin/theme/colors/{id}")
    suspend fun updateAccentColor(
        @Path("id") id: Long,
        @Body request: AccentColorRequest
    ): DataResultResponse<AccentColorAdminDto>

    @PATCH("api/admin/theme/colors/{id}/active")
    suspend fun setAccentColorActive(
        @Path("id") id: Long,
        @Body request: UpdateAccentColorActiveRequest
    ): ResultResponse

    @PUT("api/admin/theme/grid-size")
    suspend fun updateThemeGridSize(@Body request: ThemeGridSizeRequest): ResultResponse
}
