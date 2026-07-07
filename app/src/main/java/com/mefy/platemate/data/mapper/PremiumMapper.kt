package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.premium.PremiumCatalogDto
import com.mefy.platemate.data.remote.dto.premium.PremiumFeatureDto
import com.mefy.platemate.data.remote.dto.premium.PremiumPlanDto
import com.mefy.platemate.domain.model.premium.PremiumCatalog
import com.mefy.platemate.domain.model.premium.PremiumFeature
import com.mefy.platemate.domain.model.premium.PremiumPeriod
import com.mefy.platemate.domain.model.premium.PremiumPlan
import java.util.Locale
import javax.inject.Inject

/**
 * Maps the server catalog to the domain model.
 * The backend now resolves the locale automatically based on Accept-Language.
 */
class PremiumMapper @Inject constructor() : Mapper<PremiumCatalogDto, PremiumCatalog> {

    override fun map(input: PremiumCatalogDto): PremiumCatalog {
        val plans = input.plans.orEmpty()
            .sortedBy { it.sortOrder ?: 0 }
            .map(::mapPlan)
        val features = input.features.orEmpty()
            .sortedBy { it.sortOrder ?: 0 }
            .map(::mapFeature)
        return PremiumCatalog(plans = plans, features = features)
    }

    private fun mapPlan(dto: PremiumPlanDto): PremiumPlan = PremiumPlan(
        id = dto.id,
        period = PremiumPeriod.fromString(dto.period),
        amount = dto.amount ?: 0.0,
        currency = dto.currency.orEmpty().ifBlank { "TRY" },
        discountPercent = dto.discountPercent?.takeIf { it > 0 }
    )

    private fun mapFeature(dto: PremiumFeatureDto): PremiumFeature {
        return PremiumFeature(
            id = dto.id,
            iconKey = dto.iconKey.orEmpty().ifBlank { "check" },
            title = dto.title.orEmpty(),
            subtitle = dto.subtitle?.takeIf { it.isNotBlank() }
        )
    }
}
