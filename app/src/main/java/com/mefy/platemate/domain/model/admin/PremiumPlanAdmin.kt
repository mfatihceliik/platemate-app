package com.mefy.platemate.domain.model.admin

data class PremiumPlanAdmin(
    val id: Long,
    val period: String,
    val titles: Map<String, String>?,
    val descriptions: Map<String, String>?,
    val amount: Double,
    val currency: String,
    val discountPercent: Int?,
    val sortOrder: Int,
    val active: Boolean
)

/** Editable pricing payload for a plan update (period is not editable). */
data class PremiumPlanInput(
    val titles: Map<String, String>,
    val descriptions: Map<String, String>?,
    val amount: Double,
    val currency: String,
    val discountPercent: Int?,
    val sortOrder: Int
)
