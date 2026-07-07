package com.mefy.platemate.presentation.features.admin.premiumfeatures

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class PremiumFeaturesUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<PremiumFeatureListItem> = emptyList(),
    val togglingId: Long? = null
)
