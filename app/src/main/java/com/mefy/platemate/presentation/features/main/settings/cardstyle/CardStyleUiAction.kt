package com.mefy.platemate.presentation.features.main.settings.cardstyle

import com.mefy.platemate.presentation.components.variant.PMPlateCardStyle

sealed interface CardStyleUiAction {
    data class StyleSelected(val style: PMPlateCardStyle) : CardStyleUiAction
    data object PremiumCtaClicked : CardStyleUiAction
    data object BackClicked : CardStyleUiAction
}
