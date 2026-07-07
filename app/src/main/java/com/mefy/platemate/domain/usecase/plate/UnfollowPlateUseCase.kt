package com.mefy.platemate.domain.usecase.plate

import com.mefy.platemate.domain.repository.PlateRepository
import javax.inject.Inject

class UnfollowPlateUseCase @Inject constructor(
    private val repository: PlateRepository
) {
    suspend operator fun invoke(plateCode: String) = repository.unfollowPlate(plateCode)
}
