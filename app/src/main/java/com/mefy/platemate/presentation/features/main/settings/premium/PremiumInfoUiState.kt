package com.mefy.platemate.presentation.features.main.settings.premium

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class PremiumInfoUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val premiumActive: Boolean = false,
    val premiumUntilText: String = "-"
)
