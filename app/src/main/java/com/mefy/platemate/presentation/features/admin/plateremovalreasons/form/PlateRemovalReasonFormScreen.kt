package com.mefy.platemate.presentation.features.admin.plateremovalreasons.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PlateRemovalReasonFormScreen(
    modifier: Modifier = Modifier,
    state: PlateRemovalReasonFormUiState,
    onAction: (PlateRemovalReasonFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    val onSaveClicked = remember(onAction) { { onAction(PlateRemovalReasonFormUiAction.SaveClicked) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            item {
                PMSectionLabel(text = stringResource(R.string.admin_plate_removal_reason_field_code))
                FormField(state.code, !state.isEdit) { onAction(PlateRemovalReasonFormUiAction.CodeChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_plate_removal_reason_field_label))
                FormField(state.label) { onAction(PlateRemovalReasonFormUiAction.LabelChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_plate_removal_reason_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(PlateRemovalReasonFormUiAction.SortOrderChanged(it))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.s8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.s12)
                ) {
                    PMText(
                        text = stringResource(R.string.admin_plate_removal_reason_field_requires_description),
                        style = PMTextStyle.Body,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    PMSwitch(
                        checked = state.requiresDescription,
                        onCheckedChange = { onAction(PlateRemovalReasonFormUiAction.RequiresDescriptionChanged(it)) }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.s8),
            verticalArrangement = Arrangement.spacedBy(spacing.s8)
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

private val PlateRemovalReasonFormPreviewState = PlateRemovalReasonFormUiState(
    isLoading = false,
    isEdit = true,
    code = "OTHER",
    label = "Diğer",
    requiresDescription = true,
    sortOrder = "7"
)

@Preview(name = "PlateRemovalReasonForm Edit Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateRemovalReasonFormScreenEditLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalReasonFormScreen(
            state = PlateRemovalReasonFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PlateRemovalReasonForm Edit Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PlateRemovalReasonFormScreenEditDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PlateRemovalReasonFormScreen(
            state = PlateRemovalReasonFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PlateRemovalReasonForm Add", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PlateRemovalReasonFormScreenAddPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PlateRemovalReasonFormScreen(
            state = PlateRemovalReasonFormUiState(isEdit = false),
            onAction = {}
        )
    }
}

