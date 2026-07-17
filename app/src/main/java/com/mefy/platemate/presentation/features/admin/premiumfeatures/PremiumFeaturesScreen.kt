package com.mefy.platemate.presentation.features.admin.premiumfeatures

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
import com.mefy.platemate.presentation.features.admin.premiumfeatures.components.PremiumFeatureRow
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PremiumFeaturesScreen(
    state: PremiumFeaturesUiState,
    onAction: (PremiumFeaturesUiAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_premium_features_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            items(items = state.items, key = { it.id }) { item ->
                PremiumFeatureRow(
                    item = item,
                    onClick = { onAction(PremiumFeaturesUiAction.EditClicked(item.id)) },
                    onToggle = { onAction(PremiumFeaturesUiAction.ActiveToggled(item.id, item.active)) }
                )
            }
        }
    }
}
