package com.mefy.platemate.presentation.features.admin.accentcolors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.accentcolors.components.AccentColorRow
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun AccentColorsScreen(
    modifier: Modifier = Modifier,
    state: AccentColorsUiState,
    onAction: (AccentColorsUiAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        item {
            PMSectionLabel(text = stringResource(R.string.admin_theme_grid_size))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    FormField(state.gridSizeInput, keyboardType = KeyboardType.Number) {
                        onAction(AccentColorsUiAction.GridSizeChanged(it))
                    }
                }
                PMButton(
                    text = stringResource(R.string.common_save),
                    onClick = { onAction(AccentColorsUiAction.GridSizeSaveClicked) },
                    variant = PMButtonVariant.Outlined,
                    enabled = state.isGridSizeSaveEnabled,
                    loading = state.savingGridSize,
                    modifier = Modifier.padding(top = spacing.s4)
                )
            }
        }

        item {
            PMSectionLabel(text = stringResource(R.string.admin_theme_colors))
        }

        if (state.items.isEmpty()) {
            item {
                PMText(
                    text = stringResource(R.string.admin_theme_colors_empty),
                    style = PMTextStyle.Body,
                    color = colors.textLabel,
                    modifier = Modifier.fillMaxWidth().padding(spacing.s16)
                )
            }
        } else {
            items(items = state.items, key = { it.id }) { item ->
                AccentColorRow(
                    item = item,
                    onClick = { onAction(AccentColorsUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(AccentColorsUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}

private val accentColorsPreviewState = AccentColorsUiState(
    isLoading = false,
    items = listOf(
        AccentColorListItem(id = 1L, hex = "#06B6D4", sortOrder = 0, active = true),
        AccentColorListItem(id = 2L, hex = "#7C3AED", sortOrder = 1, active = true),
        AccentColorListItem(id = 3L, hex = "#EA580C", sortOrder = 2, active = false)
    ),
    gridSizeInput = "4"
)

@Preview(name = "AccentColors Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccentColorsScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AccentColorsScreen(state = accentColorsPreviewState, onAction = {})
    }
}

@Preview(name = "AccentColors Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AccentColorsScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AccentColorsScreen(state = accentColorsPreviewState, onAction = {})
    }
}

@Preview(name = "AccentColors Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccentColorsScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AccentColorsScreen(state = AccentColorsUiState(isLoading = false), onAction = {})
    }
}
