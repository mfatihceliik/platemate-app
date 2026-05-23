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
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PlateReviewRepositoryImpl @Inject constructor(
    private val api: ReviewApiService,
    private val plateReviewPageMapper: PlateReviewPageMapper,
    private val appDispatchers: AppDispatchers
) : PlateReviewRepository {

    override suspend fun addReview(plateCode: String, rating: Int, comment: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall {
                api.addOrUpdateMyReviewForPlate(
                    plateCode,
                    AddPlateReviewRequest(rating = rating, comment = comment.orEmpty())
                )
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
