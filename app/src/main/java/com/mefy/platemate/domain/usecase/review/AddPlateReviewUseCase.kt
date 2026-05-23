package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class AddPlateReviewUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(plateCode: String, rating: Int, comment: String?) =
        repository.addReview(plateCode.trim(), rating, comment?.trim())
}
