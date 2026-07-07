package com.mefy.platemate.presentation.features.admin.premiumplans.form

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
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.reporttypes.components.FormField
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PremiumPlanFormScreen(
    modifier: Modifier = Modifier,
    state: PremiumPlanFormUiState,
    onAction: (PremiumPlanFormUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val onSaveClicked = remember(onAction) { { onAction(PremiumPlanFormUiAction.SaveClicked) } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(dims.spacing.s16),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
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
