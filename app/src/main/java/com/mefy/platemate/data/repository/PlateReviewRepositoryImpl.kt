package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.mapper.PlateReviewPageMapper
import com.mefy.platemate.data.remote.rest.service.ReviewApiService
import com.mefy.platemate.data.remote.dto.plate.AddPlateReviewRequest
import com.mefy.platemate.data.remote.dto.review.UpdatePlateReviewRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeMessageCall
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.review.ReviewResponse
import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PlateReviewRepositoryImpl @Inject constructor(
    private val api: ReviewApiService,
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

    override suspend fun getPlateReviews(plateCode: String, page: Int, size: Int): AppResult<PagedResult<Review>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getPlateReviews(plateCode, page, size) }.map(plateReviewPageMapper::map)
        }

    override suspend fun updateReview(id: Long, rating: Int, comment: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall {
                api.updateReview(
                    id,
                    UpdatePlateReviewRequest(rating = rating, comment = comment.orEmpty())
                )
            }
        }

    override suspend fun deleteReview(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.deleteReview(id) }
        }
}
