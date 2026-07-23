package com.mefy.platemate.presentation.features.admin.premiumfeatures.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.features.admin.components.AddLanguageRow
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PremiumFeatureFormScreen(
    modifier: Modifier = Modifier,
    state: PremiumFeatureFormUiState,
    onAction: (PremiumFeatureFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {

    val spacing = PMTheme.spacing
    val onSaveClicked = remember(onAction) { { onAction(PremiumFeatureFormUiAction.SaveClicked) } }

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

private val premiumFeatureFormPreviewState = PremiumFeatureFormUiState(
    isLoading = false,
    isEdit = true,
    iconKey = "star",
    titles = mapOf("tr" to "Sınırsız Sorgu", "en" to "Unlimited Lookups"),
    subtitles = mapOf("tr" to "Dilediğin kadar plaka sorgula", "en" to "Look up as many plates as you want"),
    sortOrder = "1"
)

@Preview(name = "PremiumFeatureForm Edit Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PremiumFeatureFormScreenEditLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumFeatureFormScreen(
            state = premiumFeatureFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumFeatureForm Edit Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PremiumFeatureFormScreenEditDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PremiumFeatureFormScreen(
            state = premiumFeatureFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumFeatureForm Add", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PremiumFeatureFormScreenAddPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumFeatureFormScreen(
            state = PremiumFeatureFormUiState(isEdit = false),
            onAction = {}
        )
    }
}
