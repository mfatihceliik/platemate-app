package com.mefy.platemate.presentation.features.admin.accentcolors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.accentcolors.components.AccentColorRow
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AccentColorsScreen(
    state: AccentColorsUiState,
    onAction: (AccentColorsUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(contentPadding),
        contentPadding = PaddingValues(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        item {
            PMSectionLabel(text = stringResource(R.string.admin_theme_grid_size))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
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
                    modifier = Modifier.padding(top = dims.spacing.s4)
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
                    modifier = Modifier.fillMaxWidth().padding(dims.spacing.s16)
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
