package com.mefy.platemate.presentation.features.main.profile.settings.language

import com.mefy.platemate.domain.model.language.AppLanguage

sealed interface LanguageUiAction {
    data class SearchQueryChanged(val query: String) : LanguageUiAction
    data class LanguageSelected(val language: AppLanguage) : LanguageUiAction
    data object BackClicked : LanguageUiAction
}
