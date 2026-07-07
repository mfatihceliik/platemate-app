package com.mefy.platemate.domain.model.premium

/** Backend-driven premium catalog: pricing plans + feature bullets, already locale-resolved. */
data class PremiumCatalog(
    val plans: List<PremiumPlan>,
    val features: List<PremiumFeature>
)

enum class PremiumPeriod { MONTHLY, YEARLY, UNKNOWN;

    companion object {
        fun fromString(value: String?): PremiumPeriod = when (value?.uppercase()) {
            "MONTHLY" -> MONTHLY
            "YEARLY" -> YEARLY
            else -> UNKNOWN
        }
    }
}

data class PremiumPlan(
    val id: Long,
    val period: PremiumPeriod,
    val amount: Double,
    val currency: String,
    val discountPercent: Int?
)

data class PremiumFeature(
    val id: Long,
    val iconKey: String,
    val title: String,
    val subtitle: String?
)
