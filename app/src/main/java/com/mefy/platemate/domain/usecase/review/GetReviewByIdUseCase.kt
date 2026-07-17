package com.mefy.platemate.domain.usecase.review

import com.mefy.platemate.domain.repository.PlateReviewRepository
import javax.inject.Inject

class GetReviewByIdUseCase @Inject constructor(
    private val repository: PlateReviewRepository
) {
    suspend operator fun invoke(id: Long) = repository.getReviewById(id)
}
