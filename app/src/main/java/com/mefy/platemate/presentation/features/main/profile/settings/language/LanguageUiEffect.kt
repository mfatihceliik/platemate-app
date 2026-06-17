package com.mefy.platemate.presentation.features.main.profile.settings.language

sealed interface LanguageUiEffect {
    data object NavigateBack : LanguageUiEffect
}
