package com.mefy.platemate.presentation.features.admin.accentcolors

sealed interface AccentColorsUiEffect {
    data object NavigateBack : AccentColorsUiEffect
    data class NavigateToForm(val colorId: Long?) : AccentColorsUiEffect
}
