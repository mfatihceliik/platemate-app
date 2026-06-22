package com.mefy.platemate.presentation.features.main.settings.themecolor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.AccentColorGrid
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.AppearanceSelector
import com.mefy.platemate.presentation.features.main.settings.themecolor.components.ThemePreviewCard
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun ThemeColorScreen(
    state: ThemeColorUiState,
    onAction: (ThemeColorUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.profile_theme_color_title),
            onBackClick = { onAction(ThemeColorUiAction.BackClicked) }
        ),
        containerColor = colors.background
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + dims.spacing.s16,
                bottom = innerPadding.calculateBottomPadding() + dims.spacing.s16,
                start = innerPadding.calculateStartPadding(layoutDirection) + dims.spacing.s16,
                end = innerPadding.calculateEndPadding(layoutDirection) + dims.spacing.s16
            ),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
        ) {
            item(key = "preview") {
                SectionLabel(text = stringResource(R.string.profile_theme_preview))
                ThemePreviewCard(modifier = Modifier.fillMaxWidth())
            }

            item(key = "color_grid") {
                SectionLabel(text = stringResource(R.string.profile_theme_accent_color))
                AccentColorGrid(
                    colors = AccentColors,
                    selectedColor = state.selectedColor,
                    onColorSelected = { onAction(ThemeColorUiAction.ColorSelected(it)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(key = "appearance") {
                SectionLabel(text = stringResource(R.string.profile_theme_appearance))
                AppearanceSelector(
                    themeMode = state.themeMode,
                    onModeSelected = { onAction(ThemeColorUiAction.ThemeModeSelected(it)) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeColorPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ThemeColorScreen(
            state = ThemeColorUiState(),
            onAction = {}
        )
    }
}
