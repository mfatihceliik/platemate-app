package com.mefy.platemate.presentation.features.main.profile.settings.themecolor

import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.theme.AppThemeMode

sealed interface ThemeColorUiAction {
    data class ColorSelected(val color: Color) : ThemeColorUiAction
    data class ThemeModeSelected(val mode: AppThemeMode) : ThemeColorUiAction
    data object BackClicked : ThemeColorUiAction
}
