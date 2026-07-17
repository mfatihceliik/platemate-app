package com.mefy.platemate.domain.usecase.plate

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.plate.PlateRemovalReason
import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject

class GetPlateRemovalReasonsUseCase @Inject constructor(
    private val plateRepository: PlateRepository
) {
    suspend operator fun invoke(): AppResult<List<PlateRemovalReason>> {
        return plateRepository.getPlateRemovalReasons()
    }
}
