package com.mefy.platemate.presentation.features.admin.reporttypes.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

private val SEVERITY_OPTIONS = listOf("RED", "YELLOW")

@Composable
internal fun ReportTypeFormScreen(
    modifier: Modifier = Modifier,
    state: ReportTypeFormUiState,
    onAction: (ReportTypeFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val spacing = PMTheme.spacing

    val onSaveClicked = remember(onAction) { { onAction(ReportTypeFormUiAction.SaveClicked) } }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_code))
                FormField(state.code, !state.isEdit) {
                    onAction(
                        ReportTypeFormUiAction.CodeChanged(
                            it
                        )
                    )
                }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_label))
                FormField(state.label) { onAction(ReportTypeFormUiAction.LabelChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_description))
                FormField(state.description) { onAction(ReportTypeFormUiAction.DescriptionChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_icon))
                FormField(state.iconKey) { onAction(ReportTypeFormUiAction.IconKeyChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_severity))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8),
                ) {
                    items(items = SEVERITY_OPTIONS, key = { it }) { option ->
                        PMButton(
                            text = option,
                            onClick = { onAction(ReportTypeFormUiAction.SeverityChanged(option)) },
                            variant = if (state.severityCode == option) PMButtonVariant.Filled else PMButtonVariant.Outlined,
                            modifier = Modifier.wrapContentWidth()
                        )
                    }
                }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_color))
                FormField(state.colorHex) { onAction(ReportTypeFormUiAction.ColorHexChanged(it)) }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_weight))
                FormField(state.weight, keyboardType = KeyboardType.Number) {
                    onAction(
                        ReportTypeFormUiAction.WeightChanged(it)
                    )
                }
            }

            item {
                PMSectionLabel(text = stringResource(R.string.admin_report_type_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(
                        ReportTypeFormUiAction.SortOrderChanged(it)
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

private val reportTypeFormPreviewState = ReportTypeFormUiState(
    isLoading = false,
    isEdit = true,
    code = "SPAM",
    label = "Spam",
    description = "Alakasız reklam veya tekrarlı içerik.",
    iconKey = "spam",
    severityCode = "YELLOW",
    colorHex = "#FFCC00",
    weight = "3",
    sortOrder = "1"
)

@Preview(name = "ReportTypeForm Edit Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReportTypeFormScreenEditLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportTypeFormScreen(
            state = reportTypeFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "ReportTypeForm Edit Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ReportTypeFormScreenEditDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ReportTypeFormScreen(
            state = reportTypeFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "ReportTypeForm Add", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ReportTypeFormScreenAddPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ReportTypeFormScreen(
            state = ReportTypeFormUiState(isEdit = false),
            onAction = {}
        )
    }
}
