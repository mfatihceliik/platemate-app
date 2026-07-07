package com.mefy.platemate.domain.usecase.premium

import com.mefy.platemate.domain.repository.PremiumRepository
import javax.inject.Inject

class GetPremiumCatalogUseCase @Inject constructor(
    private val repository: PremiumRepository
) {
    suspend operator fun invoke() = repository.getCatalog()
}
