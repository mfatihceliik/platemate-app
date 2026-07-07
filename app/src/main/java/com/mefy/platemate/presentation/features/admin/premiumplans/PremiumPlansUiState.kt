package com.mefy.platemate.presentation.features.admin.premiumplans

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class PremiumPlansUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val items: List<PremiumPlanListItem> = emptyList(),
    val togglingId: Long? = null
)
