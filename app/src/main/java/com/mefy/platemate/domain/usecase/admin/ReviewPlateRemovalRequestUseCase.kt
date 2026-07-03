package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class ReviewPlateRemovalRequestUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(requestId: Long, statusCode: String, adminNote: String?) =
        repository.reviewPlateRemovalRequest(requestId, statusCode, adminNote)
}
