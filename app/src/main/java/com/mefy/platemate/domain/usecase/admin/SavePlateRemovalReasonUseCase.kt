package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.admin.PlateRemovalReasonInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SavePlateRemovalReasonUseCase @Inject constructor(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(id: Long?, input: PlateRemovalReasonInput): AppResult<Unit> {
        return if (id == null) {
            adminRepository.addPlateRemovalReason(input)
        } else {
            adminRepository.updatePlateRemovalReason(id, input)
        }
    }
}
