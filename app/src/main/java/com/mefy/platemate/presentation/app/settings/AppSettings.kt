package com.mefy.platemate.presentation.app.settings

import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.model.theme.AppThemeMode

data class AppSettings(
    val themeMode: AppThemeMode,
    val language: AppLanguage,
    val accentColor: Long
)
