package com.mefy.platemate.presentation.features.main.settings.themecolor

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class ThemeColorUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    // Draft: what the screen currently shows (previewed, not yet applied/persisted).
    val selectedColor: Color = AccentColors[0],
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    // Baseline: the last saved/applied values, from the local store.
    val savedColor: Color = AccentColors[0],
    val savedMode: AppThemeMode = AppThemeMode.SYSTEM,
    val isSaving: Boolean = false,
    val colors: List<Color> = emptyList(),
    val gridSize: Int = 4
) {
    val isDirty: Boolean get() = selectedColor != savedColor || themeMode != savedMode
    val isSaveEnabled: Boolean get() = isDirty && !isSaving
}

/** Client fallback palette (also the backend seed); the live list now comes from the server. */
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
