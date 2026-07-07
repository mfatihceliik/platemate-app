package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class GetPremiumFeaturesUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke() = repository.getPremiumFeaturesAdmin()
}
