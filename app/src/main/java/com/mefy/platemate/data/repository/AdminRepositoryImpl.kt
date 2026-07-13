package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.mapper.AdminMenuItemMapper
import com.mefy.platemate.data.mapper.AppSettingsMapper
import com.mefy.platemate.data.mapper.CommentReportMapper
import com.mefy.platemate.data.mapper.CommentReportReasonAdminMapper
import com.mefy.platemate.data.mapper.HiddenPlateMapper
import com.mefy.platemate.data.mapper.PendingCommentMapper
import com.mefy.platemate.data.mapper.PlateRemovalRequestMapper
import com.mefy.platemate.data.mapper.AccentColorAdminMapper
import com.mefy.platemate.data.mapper.PremiumFeatureAdminMapper
import com.mefy.platemate.data.mapper.PremiumPlanAdminMapper
import com.mefy.platemate.data.mapper.ReportTypeAdminMapper
import com.mefy.platemate.data.mapper.SocialPlatformAdminMapper
import com.mefy.platemate.data.remote.dto.admin.AdminCommentModerationRequest
import com.mefy.platemate.data.remote.dto.admin.AdminReviewRequest
import com.mefy.platemate.data.remote.dto.admin.CommentReportReasonRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateCommentReportReasonActiveRequest
import com.mefy.platemate.data.remote.dto.admin.HidePlateRequest
import com.mefy.platemate.data.remote.dto.admin.AccentColorRequest
import com.mefy.platemate.data.remote.dto.admin.ThemeGridSizeRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateAccentColorActiveRequest
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeRequest
import com.mefy.platemate.data.remote.dto.admin.PremiumFeatureRequest
import com.mefy.platemate.data.remote.dto.admin.PremiumPlanRequest
import com.mefy.platemate.data.remote.dto.admin.SocialPlatformRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateAppSettingsRequest
import com.mefy.platemate.data.remote.dto.admin.UpdatePremiumActiveRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateReportTypeActiveRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateSocialPlatformActiveRequest
import com.mefy.platemate.data.remote.rest.service.AdminApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.admin.AdminMenuItem
import com.mefy.platemate.domain.model.admin.AppSettings
import com.mefy.platemate.domain.model.admin.CommentReport
import com.mefy.platemate.domain.model.admin.CommentReportReasonAdmin
import com.mefy.platemate.domain.model.admin.CommentReportReasonInput
import com.mefy.platemate.domain.model.admin.HiddenPlate
import com.mefy.platemate.domain.model.admin.PendingComment
import com.mefy.platemate.domain.model.admin.PlateRemovalRequest
import com.mefy.platemate.domain.model.admin.AccentColorAdmin
import com.mefy.platemate.domain.model.admin.AccentColorInput
import com.mefy.platemate.domain.model.admin.PremiumFeatureAdmin
import com.mefy.platemate.domain.model.admin.PremiumFeatureInput
import com.mefy.platemate.domain.model.admin.PremiumPlanAdmin
import com.mefy.platemate.domain.model.admin.PremiumPlanInput
import com.mefy.platemate.domain.model.admin.ReportTypeAdmin
import com.mefy.platemate.domain.model.admin.ReportTypeInput
import com.mefy.platemate.domain.model.admin.SocialPlatformInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class AdminRepositoryImpl @Inject constructor(
    private val api: AdminApiService,
    private val pendingCommentMapper: PendingCommentMapper,
    private val commentReportMapper: CommentReportMapper,
    private val plateRemovalRequestMapper: PlateRemovalRequestMapper,
    private val hiddenPlateMapper: HiddenPlateMapper,
    private val appSettingsMapper: AppSettingsMapper,
    private val reportTypeAdminMapper: ReportTypeAdminMapper,
    private val commentReportReasonAdminMapper: CommentReportReasonAdminMapper,
    private val adminMenuItemMapper: AdminMenuItemMapper,
    private val socialPlatformAdminMapper: SocialPlatformAdminMapper,
    private val premiumPlanAdminMapper: PremiumPlanAdminMapper,
    private val premiumFeatureAdminMapper: PremiumFeatureAdminMapper,
    private val accentColorAdminMapper: AccentColorAdminMapper,
    private val appDispatchers: AppDispatchers
) : AdminRepository {

    override suspend fun getAdminMenu(): AppResult<List<AdminMenuItem>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getAdminMenu() }.map { dtos -> dtos.map(adminMenuItemMapper::map) }
        }

    override suspend fun getPendingComments(page: Int, size: Int): AppResult<PagedResult<PendingComment>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPendingComments(page, size) }.map { paged ->
                PagedResult(pendingCommentMapper.mapList(paged.items), paged.meta)
            }
        }

    override suspend fun approveComment(commentId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.approveComment(commentId) }
        }

    override suspend fun rejectComment(commentId: Long, reason: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.rejectComment(commentId, AdminCommentModerationRequest(reason)) }
        }

    override suspend fun removeComment(commentId: Long, reason: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.removeComment(commentId, AdminCommentModerationRequest(reason)) }
        }

    override suspend fun getCommentReports(page: Int, size: Int): AppResult<PagedResult<CommentReport>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getCommentReports(page, size) }.map { paged ->
                PagedResult(commentReportMapper.mapList(paged.items), paged.meta)
            }
        }

    override suspend fun reviewCommentReport(reportId: Long, statusCode: String, adminNote: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.reviewCommentReport(reportId, AdminReviewRequest(statusCode, adminNote)) }
        }

    override suspend fun getPlateRemovalRequests(page: Int, size: Int): AppResult<PagedResult<PlateRemovalRequest>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPlateRemovalRequests(page, size) }.map { paged ->
                PagedResult(plateRemovalRequestMapper.mapList(paged.items), paged.meta)
            }
        }

    override suspend fun reviewPlateRemovalRequest(requestId: Long, statusCode: String, adminNote: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.reviewPlateRemovalRequest(requestId, AdminReviewRequest(statusCode, adminNote)) }
        }

    override suspend fun getHiddenPlates(page: Int, size: Int): AppResult<PagedResult<HiddenPlate>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getHiddenPlates(page, size) }.map { paged ->
                PagedResult(hiddenPlateMapper.mapList(paged.items), paged.meta)
            }
        }

    override suspend fun restorePlate(plateId: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.restorePlate(plateId) }
        }

    override suspend fun hidePlate(plateId: Long, reason: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.hidePlate(plateId, HidePlateRequest(reason)) }
        }

    override suspend fun getAppSettings(): AppResult<AppSettings> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getAppSettings() }.map(appSettingsMapper::map)
        }

    override suspend fun updateAppSettings(
        nonPremiumPlateFollowLimit: Int,
        nonPremiumPlateAlarmLimit: Int,
        preApprovalMessageLimit: Int,
        commentReportThreshold: Int
    ): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall {
                api.updateAppSettings(
                    UpdateAppSettingsRequest(
                        nonPremiumPlateFollowLimit = nonPremiumPlateFollowLimit,
                        nonPremiumPlateAlarmLimit = nonPremiumPlateAlarmLimit,
                        preApprovalMessageLimit = preApprovalMessageLimit,
                        commentReportThreshold = commentReportThreshold
                    )
                )
            }
        }

    override suspend fun getReportTypes(): AppResult<List<ReportTypeAdmin>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getReportTypes() }.map { dtos -> dtos.map(reportTypeAdminMapper::map) }
        }

    override suspend fun addReportType(input: ReportTypeInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.addReportType(input.toRequest()) }.map { }
        }

    override suspend fun updateReportType(id: Long, input: ReportTypeInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.updateReportType(id, input.toRequest()) }.map { }
        }

    override suspend fun setReportTypeActive(id: Long, active: Boolean): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.setReportTypeActive(id, UpdateReportTypeActiveRequest(active)) }
        }

    private fun ReportTypeInput.toRequest(): PlateReportTypeRequest = PlateReportTypeRequest(
        code = code,
        label = label,
        description = description,
        iconKey = iconKey,
        severityCode = severityCode,
        colorHex = colorHex,
        weight = weight,
        sortOrder = sortOrder
    )

    override suspend fun getCommentReportReasonsAdmin(): AppResult<List<CommentReportReasonAdmin>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getCommentReportReasons() }.map { dtos -> dtos.map(commentReportReasonAdminMapper::map) }
        }

    override suspend fun addCommentReportReason(input: CommentReportReasonInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.addCommentReportReason(input.toRequest()) }.map { }
        }

    override suspend fun updateCommentReportReason(id: Long, input: CommentReportReasonInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.updateCommentReportReason(id, input.toRequest()) }.map { }
        }

    override suspend fun setCommentReportReasonActive(id: Long, active: Boolean): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.setCommentReportReasonActive(id, UpdateCommentReportReasonActiveRequest(active)) }
        }

    private fun CommentReportReasonInput.toRequest(): CommentReportReasonRequest = CommentReportReasonRequest(
        code = code,
        label = label,
        requiresDescription = requiresDescription,
        sortOrder = sortOrder
    )

    override suspend fun getSocialPlatformsAdmin(): AppResult<List<com.mefy.platemate.domain.model.admin.SocialPlatformAdmin>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getSocialPlatformsAdmin() }.map { dtos -> dtos.map(socialPlatformAdminMapper::map) }
        }

    override suspend fun addSocialPlatform(input: SocialPlatformInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.addSocialPlatform(input.toRequest()) }.map { }
        }

    override suspend fun updateSocialPlatform(id: Long, input: SocialPlatformInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.updateSocialPlatform(id, input.toRequest()) }.map { }
        }

    override suspend fun setSocialPlatformActive(id: Long, active: Boolean): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.setSocialPlatformActive(id, UpdateSocialPlatformActiveRequest(active)) }
        }

    private fun SocialPlatformInput.toRequest(): SocialPlatformRequest = SocialPlatformRequest(
        code = code,
        labels = labels,
        iconUrl = iconUrl,
        backgroundColorHex = backgroundColorHex,
        iconTintColorHex = iconTintColorHex,
        sortOrder = sortOrder
    )

    override suspend fun getPremiumPlansAdmin(): AppResult<List<PremiumPlanAdmin>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPremiumPlans() }.map { dtos -> dtos.map(premiumPlanAdminMapper::map) }
        }

    override suspend fun updatePremiumPlan(id: Long, input: PremiumPlanInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.updatePremiumPlan(id, input.toRequest()) }.map { }
        }

    override suspend fun setPremiumPlanActive(id: Long, active: Boolean): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.setPremiumPlanActive(id, UpdatePremiumActiveRequest(active)) }
        }

    override suspend fun getPremiumFeaturesAdmin(): AppResult<List<PremiumFeatureAdmin>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPremiumFeatures() }.map { dtos -> dtos.map(premiumFeatureAdminMapper::map) }
        }

    override suspend fun addPremiumFeature(input: PremiumFeatureInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.addPremiumFeature(input.toRequest()) }.map { }
        }

    override suspend fun updatePremiumFeature(id: Long, input: PremiumFeatureInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.updatePremiumFeature(id, input.toRequest()) }.map { }
        }

    override suspend fun setPremiumFeatureActive(id: Long, active: Boolean): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.setPremiumFeatureActive(id, UpdatePremiumActiveRequest(active)) }
        }

    private fun PremiumPlanInput.toRequest(): PremiumPlanRequest = PremiumPlanRequest(
        titles = titles,
        descriptions = descriptions,
        amount = amount,
        currency = currency,
        discountPercent = discountPercent,
        sortOrder = sortOrder
    )

    private fun PremiumFeatureInput.toRequest(): PremiumFeatureRequest = PremiumFeatureRequest(
        iconKey = iconKey,
        titles = titles,
        subtitles = subtitles,
        sortOrder = sortOrder
    )

    override suspend fun getAccentColorsAdmin(): AppResult<List<AccentColorAdmin>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getAccentColors() }.map { dtos -> dtos.map(accentColorAdminMapper::map) }
        }

    override suspend fun addAccentColor(input: AccentColorInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.addAccentColor(input.toRequest()) }.map { }
        }

    override suspend fun updateAccentColor(id: Long, input: AccentColorInput): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeApiCall { api.updateAccentColor(id, input.toRequest()) }.map { }
        }

    override suspend fun setAccentColorActive(id: Long, active: Boolean): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.setAccentColorActive(id, UpdateAccentColorActiveRequest(active)) }
        }

    override suspend fun updateThemeGridSize(gridSize: Int): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.updateThemeGridSize(ThemeGridSizeRequest(gridSize)) }
        }

    private fun AccentColorInput.toRequest(): AccentColorRequest = AccentColorRequest(
        hex = hex,
        sortOrder = sortOrder
    )
}
