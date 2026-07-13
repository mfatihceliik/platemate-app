package com.mefy.platemate.presentation.features.main.settings.cardstyle

sealed interface CardStyleUiEffect {
    data object NavigateBack : CardStyleUiEffect
    data object NavigateToPremiumInfo : CardStyleUiEffect
}
