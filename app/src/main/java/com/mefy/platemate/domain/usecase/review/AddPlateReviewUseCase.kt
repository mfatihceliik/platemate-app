package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class AddPlateReviewUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(
        plateCode: String,
        rating: Int,
        comment: String?,
        reportTypeCodes: List<String>? = null
    ) = repository.addReview(plateCode.trim(), rating, comment?.trim(), reportTypeCodes)
}
