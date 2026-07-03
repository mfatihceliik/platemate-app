package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class ReportReviewUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(commentId: Long, reasonCode: String, description: String?) =
        repository.reportReview(commentId, reasonCode, description)
}
