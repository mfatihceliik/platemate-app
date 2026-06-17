package com.mefy.platemate.presentation.features.main.profile.settings.themecolor

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.theme.AppThemeMode

@Immutable
data class ThemeColorUiState(
    val selectedColor: Color = AccentColors[0],
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM
)

val AccentColors = listOf(
    Color(0xFF06B6D4), // Teal (default)
    Color(0xFF2563EB), // Blue
    Color(0xFF7C3AED), // Purple
    Color(0xFFDB2777), // Pink
    Color(0xFFE11D48), // Rose
    Color(0xFFEA580C), // Orange
    Color(0xFF16A34A), // Green
    Color(0xFF0F766E)  // Dark Teal
)
