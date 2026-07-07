package com.mefy.platemate.presentation.features.admin.premiumplans.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.common.text.PriceFormatter
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.premiumplans.PremiumPlanListItem
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun PremiumPlanRow(
    item: PremiumPlanListItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val subtitle = buildString {
        append(PriceFormatter.price(item.amount, item.currency))
        item.discountPercent?.let { append(" · %$it") }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(dims.spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
            PMText(text = item.period, style = PMTextStyle.Body, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            PMText(text = subtitle, style = PMTextStyle.Note, color = colors.textLabel)
        }
        PMSwitch(checked = item.active, onCheckedChange = { onToggle() })
    }
}
