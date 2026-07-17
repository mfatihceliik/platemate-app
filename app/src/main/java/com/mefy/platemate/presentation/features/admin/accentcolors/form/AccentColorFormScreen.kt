package com.mefy.platemate.presentation.features.admin.accentcolors.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.hexToColor
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AccentColorFormScreen(
    modifier: Modifier = Modifier,
    state: AccentColorFormUiState,
    onAction: (AccentColorFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val onSaveClicked = remember(onAction) { { onAction(AccentColorFormUiAction.SaveClicked) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            item {
                PMSectionLabel(text = stringResource(R.string.admin_theme_color_field_hex))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        FormField(state.hex) { onAction(AccentColorFormUiAction.HexChanged(it)) }
                    }
                    // Live swatch preview.
                    Box(
                        modifier = Modifier
                            .size(dims.sizing.plateBadgeSm)
                            .clip(CircleShape)
                            .background(hexToColor(state.hex))
                            .border(dims.stroke.st1, colors.outlineVariant, CircleShape)
                    )
                }
            }
            item {
                PMSectionLabel(text = stringResource(R.string.admin_theme_color_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(AccentColorFormUiAction.SortOrderChanged(it))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dims.spacing.s8),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            if (!state.isLoading) {
                PMButton(
                    text = stringResource(R.string.common_save),
                    onClick = onSaveClicked,
                    enabled = state.isSaveEnabled,
                    loading = state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(name = "AccentColorForm Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AccentColorFormLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AccentColorFormScreen(
            state = previewState(),
            onAction = {}
        )
    }
}

@Preview(name = "AccentColorForm Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AccentColorFormDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AccentColorFormScreen(
            state = previewState(),
            onAction = {}
        )
    }
}

private fun previewState() = AccentColorFormUiState(
    isLoading = false,
    isSaving = false,
    isEdit = true,
    hex = "#7C3AED",
    sortOrder = "1"
)
