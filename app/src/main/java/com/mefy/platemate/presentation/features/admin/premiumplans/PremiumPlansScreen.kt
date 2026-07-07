package com.mefy.platemate.presentation.features.admin.premiumplans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.premiumplans.components.PremiumPlanRow
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PremiumPlansScreen(
    state: PremiumPlansUiState,
    onAction: (PremiumPlansUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_premium_plans_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
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
