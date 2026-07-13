package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.AdminMenuItem
import com.mefy.platemate.domain.model.admin.AppSettings
import com.mefy.platemate.domain.model.admin.CommentReport
import com.mefy.platemate.domain.model.admin.CommentReportReasonAdmin
import com.mefy.platemate.domain.model.admin.CommentReportReasonInput
import com.mefy.platemate.domain.model.admin.HiddenPlate
import com.mefy.platemate.domain.model.admin.PendingComment
import com.mefy.platemate.domain.model.admin.AccentColorAdmin
import com.mefy.platemate.domain.model.admin.AccentColorInput
import com.mefy.platemate.domain.model.admin.PlateRemovalRequest
import com.mefy.platemate.domain.model.admin.PremiumFeatureAdmin
import com.mefy.platemate.domain.model.admin.PremiumFeatureInput
import com.mefy.platemate.domain.model.admin.PremiumPlanAdmin
import com.mefy.platemate.domain.model.admin.PremiumPlanInput
import com.mefy.platemate.domain.model.admin.ReportTypeAdmin
import com.mefy.platemate.domain.model.admin.ReportTypeInput
import com.mefy.platemate.domain.model.admin.SocialPlatformAdmin
import com.mefy.platemate.domain.model.admin.SocialPlatformInput

interface AdminRepository {
    suspend fun getAdminMenu(): AppResult<List<AdminMenuItem>>

    suspend fun getPendingComments(page: Int, size: Int): AppResult<PagedResult<PendingComment>>
    suspend fun approveComment(commentId: Long): AppResult<Unit>
    suspend fun rejectComment(commentId: Long, reason: String?): AppResult<Unit>
    suspend fun removeComment(commentId: Long, reason: String?): AppResult<Unit>

    suspend fun getCommentReports(page: Int, size: Int): AppResult<PagedResult<CommentReport>>
    suspend fun reviewCommentReport(reportId: Long, statusCode: String, adminNote: String?): AppResult<Unit>

    suspend fun getPlateRemovalRequests(page: Int, size: Int): AppResult<PagedResult<PlateRemovalRequest>>
    suspend fun reviewPlateRemovalRequest(requestId: Long, statusCode: String, adminNote: String?): AppResult<Unit>

    suspend fun getHiddenPlates(page: Int, size: Int): AppResult<PagedResult<HiddenPlate>>
    suspend fun restorePlate(plateId: Long): AppResult<Unit>
    suspend fun hidePlate(plateId: Long, reason: String): AppResult<Unit>

    suspend fun getAppSettings(): AppResult<AppSettings>
    suspend fun updateAppSettings(
        nonPremiumPlateFollowLimit: Int,
        nonPremiumPlateAlarmLimit: Int,
        preApprovalMessageLimit: Int,
        commentReportThreshold: Int
    ): AppResult<Unit>

    suspend fun getReportTypes(): AppResult<List<ReportTypeAdmin>>
    suspend fun addReportType(input: ReportTypeInput): AppResult<Unit>
    suspend fun updateReportType(id: Long, input: ReportTypeInput): AppResult<Unit>
    suspend fun setReportTypeActive(id: Long, active: Boolean): AppResult<Unit>

    suspend fun getCommentReportReasonsAdmin(): AppResult<List<CommentReportReasonAdmin>>
    suspend fun addCommentReportReason(input: CommentReportReasonInput): AppResult<Unit>
    suspend fun updateCommentReportReason(id: Long, input: CommentReportReasonInput): AppResult<Unit>
    suspend fun setCommentReportReasonActive(id: Long, active: Boolean): AppResult<Unit>

    suspend fun getSocialPlatformsAdmin(): AppResult<List<SocialPlatformAdmin>>
    suspend fun addSocialPlatform(input: SocialPlatformInput): AppResult<Unit>
    suspend fun updateSocialPlatform(id: Long, input: SocialPlatformInput): AppResult<Unit>
    suspend fun setSocialPlatformActive(id: Long, active: Boolean): AppResult<Unit>

    suspend fun getPremiumPlansAdmin(): AppResult<List<PremiumPlanAdmin>>
    suspend fun updatePremiumPlan(id: Long, input: PremiumPlanInput): AppResult<Unit>
    suspend fun setPremiumPlanActive(id: Long, active: Boolean): AppResult<Unit>

    suspend fun getPremiumFeaturesAdmin(): AppResult<List<PremiumFeatureAdmin>>
    suspend fun addPremiumFeature(input: PremiumFeatureInput): AppResult<Unit>
    suspend fun updatePremiumFeature(id: Long, input: PremiumFeatureInput): AppResult<Unit>
    suspend fun setPremiumFeatureActive(id: Long, active: Boolean): AppResult<Unit>

    suspend fun getAccentColorsAdmin(): AppResult<List<AccentColorAdmin>>
    suspend fun addAccentColor(input: AccentColorInput): AppResult<Unit>
    suspend fun updateAccentColor(id: Long, input: AccentColorInput): AppResult<Unit>
    suspend fun setAccentColorActive(id: Long, active: Boolean): AppResult<Unit>
    suspend fun updateThemeGridSize(gridSize: Int): AppResult<Unit>
}
