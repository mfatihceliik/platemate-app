package com.mefy.platemate.presentation.features.main.profile.settings.premium

import androidx.compose.runtime.Immutable

@Immutable
data class PremiumInfoUiState(
    val isLoading: Boolean = true,
    val premiumActive: Boolean = false,
    val premiumUntilText: String = "-"
)
