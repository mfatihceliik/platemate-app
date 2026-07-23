package com.mefy.platemate.presentation.features.admin.premiumplans.form

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
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PremiumPlanFormScreen(
    modifier: Modifier = Modifier,
    state: PremiumPlanFormUiState,
    onAction: (PremiumPlanFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val onSaveClicked = remember(onAction) { { onAction(PremiumPlanFormUiAction.SaveClicked) } }

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
                PMText(
                    text = state.period,
                    style = PMTextStyle.SectionLabel,
                    color = colors.textLabel
                )
            }
            state.titles.forEach { (locale, value) ->
                item {
                    PMSectionLabel(text = "Title (${locale.uppercase()})")
                    FormField(value) { onAction(PremiumPlanFormUiAction.TitleChanged(locale, it)) }
                }
            }
            state.descriptions.forEach { (locale, value) ->
                item {
                    PMSectionLabel(text = "Description (${locale.uppercase()})")
                    FormField(value) { onAction(PremiumPlanFormUiAction.DescriptionChanged(locale, it)) }
                }
            }
            item {
                AddLanguageRow(onAdd = { onAction(PremiumPlanFormUiAction.AddLanguage(it)) })
            }
            item {
                PMSectionLabel(text = stringResource(R.string.admin_premium_plan_field_amount))
                FormField(state.amount, keyboardType = KeyboardType.Decimal) {
                    onAction(PremiumPlanFormUiAction.AmountChanged(it))
                }
            }
            item {
                PMSectionLabel(text = stringResource(R.string.admin_premium_plan_field_currency))
                FormField(state.currency) { onAction(PremiumPlanFormUiAction.CurrencyChanged(it)) }
            }
            item {
                PMSectionLabel(text = stringResource(R.string.admin_premium_plan_field_discount))
                FormField(state.discountPercent, keyboardType = KeyboardType.Number) {
                    onAction(PremiumPlanFormUiAction.DiscountChanged(it))
                }
            }
            item {
                PMSectionLabel(text = stringResource(R.string.admin_premium_plan_field_sort))
                FormField(state.sortOrder, keyboardType = KeyboardType.Number) {
                    onAction(PremiumPlanFormUiAction.SortOrderChanged(it))
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

private val premiumPlanFormPreviewState = PremiumPlanFormUiState(
    isLoading = false,
    period = "YEARLY",
    titles = mapOf("tr" to "Yıllık Premium", "en" to "Yearly Premium"),
    descriptions = mapOf("tr" to "En avantajlı plan", "en" to "Best value plan"),
    amount = "399.90",
    currency = "TRY",
    discountPercent = "30",
    sortOrder = "2"
)

@Preview(name = "PremiumPlanForm Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PremiumPlanFormScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumPlanFormScreen(
            state = premiumPlanFormPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumPlanForm Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PremiumPlanFormScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PremiumPlanFormScreen(
            state = premiumPlanFormPreviewState,
            onAction = {}
        )
    }
}
