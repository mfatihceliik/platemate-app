package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.model.admin.PremiumPlanInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SavePremiumPlanUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    /** Plans are edit-only (seeded MONTHLY/YEARLY); [id] is always present. */
    suspend operator fun invoke(id: Long, input: PremiumPlanInput) =
        repository.updatePremiumPlan(id, input)
}
