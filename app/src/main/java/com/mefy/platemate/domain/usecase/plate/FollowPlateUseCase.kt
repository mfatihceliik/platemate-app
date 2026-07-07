package com.mefy.platemate.domain.usecase.plate

import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject

class FollowPlateUseCase @Inject constructor(
    private val repository: PlateRepository
) {
    suspend operator fun invoke(plateCode: String) = repository.followPlate(plateCode)
}
