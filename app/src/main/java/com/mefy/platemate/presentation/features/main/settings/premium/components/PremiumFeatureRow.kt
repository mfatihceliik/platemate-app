package com.mefy.platemate.presentation.features.main.settings.premium.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.domain.model.premium.PremiumFeature
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.features.main.settings.premium.premiumFeatureIcon
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun PremiumFeatureRow(
    modifier: Modifier = Modifier,
    feature: PremiumFeature
) {
    PMRowItem(
        modifier = modifier,
        title = feature.title,
        subtitle = feature.subtitle,
        leadingIcon = premiumFeatureIcon(feature.iconKey),
        leadingIconTint = PMTheme.colors.primary,
        leadingContainerColor = PMTheme.colors.primaryContainer,
    )
}

@Preview(showBackground = true)
@Composable
private fun PremiumFeatureRowPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumFeatureRow(
            feature = PremiumFeature(
                id = 1,
                iconKey = "adfree",
                title = "Reklamsız Deneyim",
                subtitle = "Uygulamayı reklamsız kullanın"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumFeatureRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PremiumFeatureRow(
            feature = PremiumFeature(
                id = 1,
                iconKey = "adfree",
                title = "Reklamsız Deneyim",
                subtitle = "Uygulamayı reklamsız kullanın"
            )
        )
    }
}
