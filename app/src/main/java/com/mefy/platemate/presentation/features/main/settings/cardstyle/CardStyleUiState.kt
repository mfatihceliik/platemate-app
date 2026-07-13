package com.mefy.platemate.presentation.features.main.settings.cardstyle

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle

@Immutable
data class CardStyleUiState(
    val selectedStyle: PMPlateCardStyle = PMPlateCardStyle.Classic,
    val isPremium: Boolean = false
)
