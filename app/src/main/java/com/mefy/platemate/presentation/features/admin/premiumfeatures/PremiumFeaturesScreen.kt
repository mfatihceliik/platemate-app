package com.mefy.platemate.presentation.features.admin.premiumfeatures

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
import com.mefy.platemate.presentation.features.admin.premiumfeatures.components.PremiumFeatureRow
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PremiumFeaturesScreen(
    modifier: Modifier = Modifier,
    state: PremiumFeaturesUiState,
    onAction: (PremiumFeaturesUiAction) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    if (state.items.isEmpty()) {
        Box(modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            PMText(text = stringResource(R.string.admin_premium_features_empty), style = PMTextStyle.Body, color = colors.textLabel)
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
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

private val premiumFeaturesPreviewState = PremiumFeaturesUiState(
    isLoading = false,
    items = listOf(
        PremiumFeatureListItem(id = 1L, iconKey = "star", titles = mapOf("tr" to "Sınırsız Sorgu", "en" to "Unlimited Lookups"), sortOrder = 0, active = true),
        PremiumFeatureListItem(id = 2L, iconKey = "shield", titles = mapOf("tr" to "Reklamsız Deneyim", "en" to "Ad-Free Experience"), sortOrder = 1, active = true),
        PremiumFeatureListItem(id = 3L, iconKey = "bolt", titles = mapOf("tr" to "Öncelikli Destek", "en" to "Priority Support"), sortOrder = 2, active = false)
    )
)

@Preview(name = "PremiumFeatures Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PremiumFeaturesScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumFeaturesScreen(
            state = premiumFeaturesPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumFeatures Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PremiumFeaturesScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PremiumFeaturesScreen(
            state = premiumFeaturesPreviewState,
            onAction = {}
        )
    }
}

@Preview(name = "PremiumFeatures Empty", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PremiumFeaturesScreenEmptyPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumFeaturesScreen(
            state = PremiumFeaturesUiState(isLoading = false),
            onAction = {}
        )
    }
}
