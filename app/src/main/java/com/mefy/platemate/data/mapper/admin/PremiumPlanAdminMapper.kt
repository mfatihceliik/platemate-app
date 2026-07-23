package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PremiumPlanAdminDto
import com.mefy.platemate.domain.model.admin.PremiumPlanAdmin
import javax.inject.Inject

class PremiumPlanAdminMapper @Inject constructor() : Mapper<PremiumPlanAdminDto, PremiumPlanAdmin> {
    override fun map(input: PremiumPlanAdminDto): PremiumPlanAdmin = PremiumPlanAdmin(
        id = input.id,
        period = input.period.orEmpty(),
        titles = input.titles,
        descriptions = input.descriptions,
        amount = input.amount ?: 0.0,
        currency = input.currency.orEmpty().ifBlank { "TRY" },
        discountPercent = input.discountPercent,
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}