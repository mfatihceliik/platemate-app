package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.mapper.AppSettingsMapper
import com.mefy.platemate.data.mapper.CommentReportMapper
import com.mefy.platemate.data.mapper.HiddenPlateMapper
import com.mefy.platemate.data.mapper.PendingCommentMapper
import com.mefy.platemate.data.mapper.PlateRemovalRequestMapper
import com.mefy.platemate.data.mapper.ReportTypeAdminMapper
import com.mefy.platemate.data.mapper.SocialPlatformAdminMapper
import com.mefy.platemate.data.remote.dto.admin.AdminCommentModerationRequest
import com.mefy.platemate.data.remote.dto.admin.AdminReviewRequest
import com.mefy.platemate.data.remote.dto.admin.HidePlateRequest
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeRequest
import com.mefy.platemate.data.remote.dto.admin.SocialPlatformRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateAppSettingsRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateReportTypeActiveRequest
import com.mefy.platemate.data.remote.dto.admin.UpdateSocialPlatformActiveRequest
import com.mefy.platemate.data.remote.rest.service.AdminApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.admin.AppSettings
import com.mefy.platemate.domain.model.admin.CommentReport
import com.mefy.platemate.domain.model.admin.HiddenPlate
import com.mefy.platemate.domain.model.admin.PendingComment
import com.mefy.platemate.domain.model.admin.PlateRemovalRequest
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
    private val socialPlatformAdminMapper: SocialPlatformAdminMapper,
    private val appDispatchers: AppDispatchers
) : AdminRepository {

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
        label = label,
        iconUrl = iconUrl,
        backgroundColorHex = backgroundColorHex,
        iconTintColorHex = iconTintColorHex,
        sortOrder = sortOrder
    )
}
