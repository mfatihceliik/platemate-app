package com.mefy.platemate.presentation.features.admin.premiumfeatures

import androidx.compose.runtime.Immutable

@Immutable
data class PremiumFeatureListItem(
    val id: Long,
    val iconKey: String,
    val titles: Map<String, String>,
    val sortOrder: Int,
    val active: Boolean
)
