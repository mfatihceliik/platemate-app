package com.mefy.platemate.presentation.features.admin.premiumplans

import androidx.compose.runtime.Immutable

@Immutable
data class PremiumPlanListItem(
    val id: Long,
    val period: String,
    val amount: Double,
    val currency: String,
    val discountPercent: Int?,
    val active: Boolean
)
