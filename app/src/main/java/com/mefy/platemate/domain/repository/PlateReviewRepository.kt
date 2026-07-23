package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.report.CommentReportReason
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.review.ReviewResponse

interface PlateReviewRepository {
    suspend fun addReview(plateCode: String, rating: Int, comment: String?, reportTypeCodes: List<String>?): AppResult<ReviewResponse>
    suspend fun getReportTypes(): AppResult<List<ReportType>>
    suspend fun getCommentReportReasons(): AppResult<List<CommentReportReason>>
    suspend fun getPlateReviews(plateCode: String, page: Int, size: Int): AppResult<PagedResult<Review>>
    suspend fun getReviewById(id: Long): AppResult<Review>
    suspend fun getMyReviews(status: String?, query: String?, page: Int, size: Int): AppResult<PagedResult<Review>>
    suspend fun updateReview(id: Long, rating: Int, comment: String?, reportTypeCodes: List<String>?): AppResult<Unit>
    suspend fun deleteReview(id: Long): AppResult<Unit>
    suspend fun reportReview(commentId: Long, reasonCode: String, description: String?): AppResult<Unit>
}

