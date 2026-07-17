package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class GetMyReviewsUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(status: String?, query: String? = null, page: Int = 0, size: Int = 20) =
        repository.getMyReviews(status, query?.trim()?.ifBlank { null }, page, size)
}
