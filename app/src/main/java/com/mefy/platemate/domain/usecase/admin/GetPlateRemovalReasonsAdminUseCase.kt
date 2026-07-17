package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PlateRemovalReasonAdmin
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class GetPlateRemovalReasonsAdminUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(): AppResult<List<PlateRemovalReasonAdmin>> {
        return adminRepository.getPlateRemovalReasonsAdmin()
    }
}
