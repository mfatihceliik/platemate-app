package com.mefy.platemate.presentation.features.admin.premiumplans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.premiumplans.components.PremiumPlanRow
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PremiumPlansScreen(
    modifier: Modifier = Modifier,
    state: PremiumPlansUiState,
    onAction: (PremiumPlansUiAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_premium_plans_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                PremiumPlanRow(
                    item = item,
                    onClick = { onAction(PremiumPlansUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(PremiumPlansUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}

private val premiumPlansPreviewState = PremiumPlansUiState(
    isLoading = false,
    items = listOf(
        PremiumPlanListItem(id = 1L, period = "MONTHLY", amount = 49.90, currency = "TRY", discountPercent = null, active = true),
        PremiumPlanListItem(id = 2L, period = "YEARLY", amount = 399.90, currency = "TRY", discountPercent = 30, active = true),
        PremiumPlanListItem(id = 3L, period = "WEEKLY", amount = 19.90, currency = "TRY", discountPercent = null, active = false)
    )
)

@Preview(name = "PremiumPlans Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PremiumPlansScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumPlansScreen(
            state = premiumPlansPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumPlans Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PremiumPlansScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PremiumPlansScreen(
            state = premiumPlansPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumPlans Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PremiumPlansScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumPlansScreen(
            state = PremiumPlansUiState(isLoading = false),
            onAction = {}
        )
    }
}
