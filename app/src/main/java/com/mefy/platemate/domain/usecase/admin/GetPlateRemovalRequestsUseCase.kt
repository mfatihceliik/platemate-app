package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class GetPlateRemovalRequestsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(page: Int, size: Int = 20) = repository.getPlateRemovalRequests(page, size)
}
