package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PlateReviewMapper
import com.mefy.platemate.data.mapper.PlateReviewPageMapper
import com.mefy.platemate.data.remote.rest.service.ReviewApiService
import com.mefy.platemate.data.remote.dto.plate.AddPlateReviewRequest
import com.mefy.platemate.data.remote.dto.report.AddCommentReportRequest
import com.mefy.platemate.data.remote.dto.review.UpdatePlateReviewRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.report.CommentReportReason
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.review.ReviewResponse
import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PlateReviewRepositoryImpl @Inject constructor(
    private val api: ReviewApiService,
    private val plateReviewMapper: PlateReviewMapper,
    private val plateReviewPageMapper: PlateReviewPageMapper,
    private val appDispatchers: AppDispatchers
) : PlateReviewRepository {

    override suspend fun addReview(
        plateCode: String,
        rating: Int,
        comment: String?,
        reportTypeCodes: List<String>?
    ): AppResult<ReviewResponse> =
        withContext(appDispatchers.io) {
            safeApiCall {
                api.addOrUpdateMyReviewForPlate(
                    plateCode,
                    AddPlateReviewRequest(
                        rating = rating,
                        comment = comment.orEmpty(),
                        reportTypeCodes = reportTypeCodes
                    )
                )
            }.map { dto ->
                ReviewResponse(
                    reviewId = dto.reviewId ?: 0L,
                    plateCode = dto.plateCode.orEmpty(),
                    rating = dto.rating ?: 0,
                    comment = dto.comment.orEmpty(),
                    status = dto.status.orEmpty(),
                    userId = dto.userId ?: 0L,
                    username = dto.username.orEmpty(),
                    displayName = dto.displayName,
                    profilePhotoUrl = dto.profilePhotoUrl,
                    reportTypeCodes = dto.reportTypeCodes.orEmpty(),
                    createdAt = dto.createdAt,
                    updatedAt = dto.updatedAt
                )
            }
        }

    override suspend fun getReportTypes(): AppResult<List<ReportType>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getReportTypes() }.map { dtos ->
                dtos.mapNotNull { dto ->
                    val code = dto.code ?: return@mapNotNull null
                    val label = dto.label ?: return@mapNotNull null
                    ReportType(
                        code = code,
                        label = label,
                        description = dto.description.orEmpty(),
                        iconKey = dto.iconKey.orEmpty(),
                        severity = dto.severityCode.orEmpty(),
                        colorHex = dto.colorHex.orEmpty(),
                        weight = dto.weight ?: 0,
                        sortOrder = dto.sortOrder ?: 0
                    )
                }
            }
        }

    override suspend fun getCommentReportReasons(): AppResult<List<CommentReportReason>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getCommentReportReasons() }.map { dtos ->
                dtos.mapNotNull { dto ->
                    val code = dto.code ?: return@mapNotNull null
                    val label = dto.label ?: return@mapNotNull null
                    CommentReportReason(
                        code = code,
                        label = label,
                        requiresDescription = dto.requiresDescription == true
                    )
                }
            }
        }

    override suspend fun getPlateReviews(plateCode: String, page: Int, size: Int): AppResult<PagedResult<Review>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPlateReviews(plateCode, page, size) }.map(plateReviewPageMapper::map)
        }

    override suspend fun getReviewById(id: Long): AppResult<Review> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getReviewById(id) }.map(plateReviewMapper::map)
        }

    override suspend fun getMyReviews(status: String?, query: String?, page: Int, size: Int): AppResult<PagedResult<Review>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getMyReviews(status, query, page, size) }.map(plateReviewPageMapper::map)
        }

    override suspend fun updateReview(id: Long, rating: Int, comment: String?, reportTypeCodes: List<String>?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall {
                api.updateReview(
                    id,
                    UpdatePlateReviewRequest(rating = rating, comment = comment.orEmpty(), reportTypeCodes = reportTypeCodes)
                )
            }
        }

    override suspend fun deleteReview(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall { api.deleteReview(id) }
        }

    override suspend fun reportReview(commentId: Long, reasonCode: String, description: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall {
                api.reportComment(
                    commentId,
                    AddCommentReportRequest(reasonCode = reasonCode, description = description?.ifBlank { null })
                )
            }
        }
}
