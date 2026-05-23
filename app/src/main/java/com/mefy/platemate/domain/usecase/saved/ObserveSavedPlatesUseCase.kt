package com.mefy.platemate.domain.usecase.saved

import com.mefy.platemate.domain.repository.SavedPlateRepository
import javax.inject.Inject

class ObserveSavedPlatesUseCase @Inject constructor(
    private val repository: SavedPlateRepository
) {
    operator fun invoke() = repository.observeSavedPlates()
}
