package com.mefy.platemate.presentation.features.main.settings.language

sealed interface LanguageUiEffect {
    data object NavigateBack : LanguageUiEffect
}
