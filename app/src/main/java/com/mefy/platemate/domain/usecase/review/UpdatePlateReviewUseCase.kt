package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class UpdatePlateReviewUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(id: Long, rating: Int, comment: String?, reportTypeCodes: List<String>?) =
        repository.updateReview(id, rating, comment?.trim(), reportTypeCodes)
}
