package com.mefy.platemate.presentation.features.main.settings.themecolor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.domain.model.theme.AppThemeMode
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.AccentColorGrid
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.AppearanceSelector
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.ThemePreviewCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ThemeColorScreen(
    state: ThemeColorUiState,
    onAction: (ThemeColorUiAction) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions

    val onColorSelected = remember(onAction) { { c: Color -> onAction(ThemeColorUiAction.ColorSelected(c)) } }
    val onModeSelected = remember(onAction) { { m: AppThemeMode -> onAction(ThemeColorUiAction.ThemeModeSelected(m)) } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        SectionLabel(text = stringResource(R.string.profile_theme_preview))
        ThemePreviewCard(modifier = Modifier.fillMaxWidth())

        SectionLabel(text = stringResource(R.string.profile_theme_accent_color))
        AccentColorGrid(
            colors = AccentColors,
            selectedColor = state.selectedColor,
            onColorSelected = onColorSelected,
            modifier = Modifier.fillMaxWidth()
        )

        SectionLabel(text = stringResource(R.string.profile_theme_appearance))
        AppearanceSelector(
            themeMode = state.themeMode,
            onModeSelected = onModeSelected
        )
    }
}

@Preview(name = "ThemeColor Content Light", showBackground = true)
@Composable
private fun ThemeColorContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ThemeColorScreen(
            state = ThemeColorUiState(isLoading = false),
            onAction = {},
        )
    }
}

@Preview(name = "ThemeColor Content Dark", showBackground = true)
@Composable
private fun ThemeColorContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ThemeColorScreen(
            state = ThemeColorUiState(isLoading = false),
            onAction = {},
        )
    }
}

@Preview(name = "ThemeColor Loading", showBackground = true)
@Composable
private fun ThemeColorLoadingPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ThemeColorScreen(
            state = ThemeColorUiState(isLoading = true),
            onAction = {},
        )
    }
}
