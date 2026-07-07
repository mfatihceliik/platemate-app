package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SaveThemeGridSizeUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(gridSize: Int) = repository.updateThemeGridSize(gridSize)
}
