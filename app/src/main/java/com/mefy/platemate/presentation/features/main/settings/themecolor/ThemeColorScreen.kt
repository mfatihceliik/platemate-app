package com.mefy.platemate.presentation.features.main.settings.themecolor


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.domain.model.theme.AppThemeMode
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.AccentColorGrid
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.AppearanceSelector
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.ThemePreviewCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ThemeColorScreen(
    modifier: Modifier = Modifier,
    state: ThemeColorUiState,
    onAction: (ThemeColorUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions

    val onColorSelected = remember(onAction) { { c: Color -> onAction(ThemeColorUiAction.ColorSelected(c)) } }
    val onModeSelected = remember(onAction) { { m: AppThemeMode -> onAction(ThemeColorUiAction.ThemeModeSelected(m)) } }
    val onSave = remember(onAction) { { onAction(ThemeColorUiAction.SaveClicked) } }
    // Server-driven palette; fall back to the bundled list if the catalog is empty.
    val colors = state.colors.ifEmpty { AccentColors }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(dims.spacing.s16),
        verticalArrangement = spacedByWithFooter(dims.spacing.s16)
    ) {
        item {
            PMSectionLabel(text = stringResource(R.string.profile_theme_preview))
        }
        item {
            ThemePreviewCard(
                modifier = Modifier.fillMaxWidth(),
                accent = state.selectedColor,
                themeMode = state.themeMode
            )
        }
        item {
            PMSectionLabel(text = stringResource(R.string.profile_theme_accent_color))
        }
        item {
            AccentColorGrid(
                colors = colors,
                selectedColor = state.selectedColor,
                onColorSelected = onColorSelected,
                gridSize = state.gridSize,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            PMSectionLabel(text = stringResource(R.string.profile_theme_appearance))
        }
        item {
            AppearanceSelector(
                themeMode = state.themeMode,
                onModeSelected = onModeSelected
            )
        }

        item {
            PMButton(
                text = stringResource(R.string.profile_theme_save_changes),
                onClick = onSave,
                enabled = state.isSaveEnabled,
                loading = state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dims.spacing.s16)
            )
        }
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
