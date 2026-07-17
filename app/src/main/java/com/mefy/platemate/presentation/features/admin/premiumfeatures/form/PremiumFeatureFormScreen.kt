package com.mefy.platemate.presentation.features.admin.premiumfeatures.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.features.admin.components.AddLanguageRow
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PremiumFeatureFormScreen(
    modifier: Modifier = Modifier,
    state: PremiumFeatureFormUiState,
    onAction: (PremiumFeatureFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val onSaveClicked = remember(onAction) { { onAction(PremiumFeatureFormUiAction.SaveClicked) } }

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
                PMSectionLabel(text = stringResource(R.string.admin_premium_feature_field_icon))
                FormField(state.iconKey) { onAction(PremiumFeatureFormUiAction.IconKeyChanged(it)) }
            }
            state.titles.forEach { (locale, value) ->
                item {
                    PMSectionLabel(text = "Title (${locale.uppercase()})")
                    FormField(value) { onAction(PremiumFeatureFormUiAction.TitleChanged(locale, it)) }
                }
            }
            state.subtitles.forEach { (locale, value) ->
                item {
                    PMSectionLabel(text = "Subtitle (${locale.uppercase()})")
                    FormField(value) { onAction(PremiumFeatureFormUiAction.SubtitleChanged(locale, it)) }
                }
            }
            item {
                AddLanguageRow(onAdd = { onAction(PremiumFeatureFormUiAction.AddLanguage(it)) })
            }
            item {
                PMSectionLabel(text = stringResource(R.string.admin_premium_feature_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(PremiumFeatureFormUiAction.SortOrderChanged(it))
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
