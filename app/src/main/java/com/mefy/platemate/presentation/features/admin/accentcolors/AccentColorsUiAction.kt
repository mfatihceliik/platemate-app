package com.mefy.platemate.presentation.features.admin.accentcolors

sealed interface AccentColorsUiAction {
    data object BackClicked : AccentColorsUiAction
    data object RetryClicked : AccentColorsUiAction
    data object AddClicked : AccentColorsUiAction
    data class EditClicked(val id: Long) : AccentColorsUiAction
    data class ActiveToggled(val id: Long, val active: Boolean) : AccentColorsUiAction
    data class GridSizeChanged(val value: String) : AccentColorsUiAction
    data object GridSizeSaveClicked : AccentColorsUiAction
}
