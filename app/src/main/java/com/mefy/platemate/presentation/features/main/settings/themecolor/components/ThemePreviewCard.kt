package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.theme.AppThemeMode
import com.mefy.platemate.presentation.components.PMPlateCard
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ThemePreviewCard(
    modifier: Modifier = Modifier,
    accent: Color? = null,
    themeMode: AppThemeMode = AppThemeMode.SYSTEM
) {
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    PlateMateTheme(
        darkTheme = isDark,
        accentColor = accent
    ) {
        PMPlateCard(
            id = "preview",
            rank = 1,
            plateNumber = "34 EK 0682",
            rating = "4.8",
            commentCount = 120,
            searchCount = 450,
            onClick = {},
            modifier = modifier
        )
    }
}
