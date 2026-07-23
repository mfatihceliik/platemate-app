package com.mefy.platemate.presentation.features.admin.premiumplans.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mefy.platemate.presentation.common.formatter.PriceFormatter
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.premiumplans.PremiumPlanListItem
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun PremiumPlanRow(
    item: PremiumPlanListItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes.medium

    val subtitle = buildString {
        append(PriceFormatter.price(item.amount, item.currency))
        item.discountPercent?.let { append(" · %$it") }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, shape)
            .clickable(onClick = onClick)
            .padding(spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.s12)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(spacing.s4)) {
            PMText(text = item.period, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = subtitle, style = PMTextStyle.Note, color = colors.textLabel)
        }
        PMSwitch(checked = item.active, onCheckedChange = { onToggle() })
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun PremiumPlanRowLightPreview() {
    com.mefy.platemate.presentation.theme.PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PremiumPlanRow(
            item = PremiumPlanListItem(
                id = 1,
                period = "Aylık",
                amount = 49.99,
                currency = "TRY",
                discountPercent = null,
                active = true
            ),
            onClick = {},
            onToggle = {}
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun PremiumPlanRowDarkPreview() {
    com.mefy.platemate.presentation.theme.PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PremiumPlanRow(
            item = PremiumPlanListItem(
                id = 2,
                period = "Yıllık",
                amount = 399.99,
                currency = "TRY",
                discountPercent = 33,
                active = false
            ),
            onClick = {},
            onToggle = {}
        )
    }
}
