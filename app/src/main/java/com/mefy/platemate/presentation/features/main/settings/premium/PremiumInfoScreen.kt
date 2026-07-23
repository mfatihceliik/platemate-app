package com.mefy.platemate.presentation.features.main.settings.premium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.settings.premium.components.PricingSection
import com.mefy.platemate.presentation.features.main.settings.premium.components.HeroSection
import com.mefy.platemate.presentation.features.main.settings.premium.components.PremiumFeatureRow
import com.mefy.platemate.domain.model.premium.PremiumFeature
import com.mefy.platemate.domain.model.premium.PremiumPeriod
import com.mefy.platemate.domain.model.premium.PremiumPlan
import com.mefy.platemate.presentation.common.spacedByWithFooter
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PremiumInfoScreen(
    modifier: Modifier = Modifier,
    state: PremiumInfoUiState,
    innerPadding: PaddingValues = PaddingValues()
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement =spacedByWithFooter(spacing.s0)
    ) {
        item {
            HeroSection(modifier = Modifier.padding(bottom = spacing.s16))
        }

        item {
            PricingSection(
                monthly = state.plans.firstOrNull { it.period == PremiumPeriod.MONTHLY },
                yearly = state.plans.firstOrNull { it.period == PremiumPeriod.YEARLY },
                modifier = Modifier.padding(bottom = spacing.s16)
            )
        }

        items(
            items = state.features,
            key = { it.id }
        ) { feature ->
            PremiumFeatureRow(feature = feature)
        }

        item {
            PMButton(
                text = stringResource(R.string.profile_premium_upgrade),
                onClick = {},
                leadingIcon = {
                    PMIcon(
                        imageVector = Icons.Filled.Star,
                        tint = Color.White,
                        size = sizing.iconMd,
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = spacing.s16)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumInfoScreen(
            state = PremiumInfoUiState(
                isLoading = false,
                plans = listOf(
                    PremiumPlan(1, PremiumPeriod.MONTHLY, 49.0, "TRY", null),
                    PremiumPlan(2, PremiumPeriod.YEARLY, 399.0, "TRY", 32)
                ),
                features = listOf(
                    PremiumFeature(1, "reviews", "Sınırsız plaka değerlendirmesi", null),
                    PremiumFeature(2, "adfree", "Reklamsız deneyim", "Hiç reklam görme"),
                    PremiumFeature(3, "stats", "Detaylı profil istatistikleri", null)
                )
            )
        )
    }
}
