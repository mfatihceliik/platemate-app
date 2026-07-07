package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.model.admin.PremiumFeatureInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SavePremiumFeatureUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    /** id == null -> create, otherwise update. */
    suspend operator fun invoke(id: Long?, input: PremiumFeatureInput) =
        if (id == null) repository.addPremiumFeature(input) else repository.updatePremiumFeature(id, input)
}
