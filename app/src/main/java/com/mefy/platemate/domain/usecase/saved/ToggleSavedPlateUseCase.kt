package com.mefy.platemate.domain.usecase.saved

import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.domain.repository.SavedPlateRepository
import javax.inject.Inject

class ToggleSavedPlateUseCase @Inject constructor(
    private val repository: SavedPlateRepository
) {
    suspend operator fun invoke(plate: SavedPlate): Boolean = repository.toggleSaved(plate)
}
