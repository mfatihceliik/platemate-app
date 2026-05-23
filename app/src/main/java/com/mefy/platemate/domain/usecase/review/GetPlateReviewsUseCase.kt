package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class GetPlateReviewsUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(plateCode: String, page: Int = 0, size: Int = 20) =
        repository.getPlateReviews(plateCode.trim(), page, size)
}
