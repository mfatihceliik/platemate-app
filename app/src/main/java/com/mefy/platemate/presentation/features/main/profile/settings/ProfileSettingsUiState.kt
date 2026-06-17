package com.mefy.platemate.presentation.features.main.profile.settings

import androidx.compose.runtime.Immutable
import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.model.theme.AppThemeMode

@Immutable
data class ProfileSettingsUiState(
    val isLoading: Boolean = true,
    val premiumActive: Boolean = false,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val language: AppLanguage = AppLanguage.TR,
    val socialLinksCount: Int = 0
)
