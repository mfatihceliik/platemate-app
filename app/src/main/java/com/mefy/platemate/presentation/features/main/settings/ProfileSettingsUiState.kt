package com.mefy.platemate.presentation.features.main.settings

import androidx.compose.runtime.Immutable
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class ProfileSettingsUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val premiumActive: Boolean = false,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.TR,
    val languageLabel: UiText = UiText.Resource(R.string.profile_setting_language_tr),
    val isAdmin: Boolean = false
)
