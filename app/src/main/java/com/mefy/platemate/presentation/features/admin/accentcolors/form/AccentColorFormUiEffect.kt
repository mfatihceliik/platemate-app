package com.mefy.platemate.presentation.features.admin.accentcolors.form

sealed interface AccentColorFormUiEffect {
    data object NavigateBack : AccentColorFormUiEffect
}
