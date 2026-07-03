package com.mefy.platemate.domain.usecase.plate

import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject

class CreatePlateRemovalRequestUseCase @Inject constructor(
    private val repository: PlateRepository
) {
    suspend operator fun invoke(plateId: Long, reasonCode: String, description: String) =
        repository.createRemovalRequest(plateId, reasonCode, description.trim())
}
