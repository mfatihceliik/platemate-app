package com.mefy.platemate.presentation.features.main.profile.settings.language

import androidx.compose.runtime.Immutable
import com.mefy.platemate.domain.model.language.AppLanguage

@Immutable
data class LanguageUiState(
    val selectedLanguage: AppLanguage = AppLanguage.TR,
    val searchQuery: String = ""
) {
    val filteredLanguages: List<LanguageItem>
        get() {
            val all = allLanguages
            if (searchQuery.isBlank()) return all
            val query = searchQuery.lowercase()
            return all.filter {
                it.displayName.lowercase().contains(query) ||
                        it.nativeName.lowercase().contains(query)
            }
        }
}

@Immutable
data class LanguageItem(
    val language: AppLanguage?,
    val displayName: String,
    val nativeName: String
)

val allLanguages = listOf(
    LanguageItem(AppLanguage.TR, "Türkçe", "Turkish"),
    LanguageItem(AppLanguage.EN, "English", "English"),
    LanguageItem(null, "Deutsch", "German"),
    LanguageItem(null, "Français", "French"),
    LanguageItem(null, "Español", "Spanish"),
    LanguageItem(null, "العربية", "Arabic")
)
