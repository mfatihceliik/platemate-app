package com.mefy.platemate.presentation.features.main.settings.premium.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.domain.model.premium.PremiumPeriod
import com.mefy.platemate.domain.model.premium.PremiumPlan
import com.mefy.platemate.presentation.common.text.PriceFormatter
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PricingSection(
    modifier: Modifier = Modifier,
    monthly: PremiumPlan?,
    yearly: PremiumPlan?
) {
    val colors = MaterialTheme.pmColors
    val shapes = MaterialTheme.shapes
    val dims = MaterialTheme.pmDimensions

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        if (monthly != null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(colors.surface, shapes.large)
                    .border(dims.stroke.st1, colors.cardBorder, shapes.large)
                    .padding(dims.spacing.s12),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = stringResource(R.string.profile_premium_monthly),
                    style = PMTextStyle.Note,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textSecondary
                )
                PMText(
                    text = PriceFormatter.price(monthly.amount, monthly.currency) + stringResource(R.string.premium_period_monthly_suffix),
                    style = PMTextStyle.Headline,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.textPrimary
                )
            }
        }

        if (yearly != null) {
            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.primaryContainer, shapes.large)
                        .padding(dims.spacing.s12),
                    verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
                ) {
                    PMText(
                        text = stringResource(R.string.profile_premium_yearly),
                        style = PMTextStyle.Note,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textSecondary
                    )
                    PMText(
                        text = PriceFormatter.price(yearly.amount, yearly.currency) + stringResource(R.string.premium_period_yearly_suffix),
                        style = PMTextStyle.Headline,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.textPrimary
                    )
                }
                yearly.discountPercent?.let { discount ->
                    PMText(
                        text = stringResource(R.string.premium_discount_format, discount),
                        style = PMTextStyle.Note,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.onPrimary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = dims.spacing.s12)
                            .clip(shapes.extraLarge)
                            .background(colors.primary)
                            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s4)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PricingSectionPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PricingSection(
            monthly = PremiumPlan(1, PremiumPeriod.MONTHLY, 49.0, "TRY", null),
            yearly = PremiumPlan(2, PremiumPeriod.YEARLY, 399.0, "TRY", 32)
        )
    }
}