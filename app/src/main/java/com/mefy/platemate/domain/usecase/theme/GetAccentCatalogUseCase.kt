package com.mefy.platemate.domain.usecase.theme

import com.mefy.platemate.domain.repository.ThemeRepository
import javax.inject.Inject

class GetAccentCatalogUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke() = repository.getCatalog()
}
