package com.mefy.platemate.presentation.features.admin.accentcolors.form

sealed interface AccentColorFormUiAction {
    data object BackClicked : AccentColorFormUiAction
    data object SaveClicked : AccentColorFormUiAction
    data class HexChanged(val value: String) : AccentColorFormUiAction
    data class SortOrderChanged(val value: String) : AccentColorFormUiAction
}
