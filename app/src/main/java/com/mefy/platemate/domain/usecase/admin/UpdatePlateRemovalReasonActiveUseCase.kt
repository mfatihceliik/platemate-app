package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class UpdatePlateRemovalReasonActiveUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(id: Long, active: Boolean): AppResult<Unit> {
        return adminRepository.setPlateRemovalReasonActive(id, active)
    }
}
