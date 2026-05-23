package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.domain.model.review.Review

interface PlateReviewRepository {
    suspend fun addReview(plateCode: String, rating: Int, comment: String?): AppResult<Unit>
    suspend fun getPlateReviews(plateCode: String, page: Int, size: Int): AppResult<PagedResult<Review>>
    suspend fun updateReview(id: Long, rating: Int, comment: String?): AppResult<Unit>
    suspend fun deleteReview(id: Long): AppResult<Unit>
}

