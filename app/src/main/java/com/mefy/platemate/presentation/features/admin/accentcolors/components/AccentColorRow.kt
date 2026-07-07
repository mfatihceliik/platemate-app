package com.mefy.platemate.presentation.features.admin.accentcolors.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.common.hexToColor
import com.mefy.platemate.presentation.components.PMSwitch
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.admin.accentcolors.AccentColorListItem
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AccentColorRow(
    item: AccentColorListItem,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(dims.spacing.s16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.plateBadgeSmall)
                .clip(CircleShape)
                .background(hexToColor(item.hex))
                .border(dims.stroke.st1, colors.outlineVariant, CircleShape)
        )
        PMText(
            text = item.hex,
            style = PMTextStyle.Body,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
        PMSwitch(checked = item.active, onCheckedChange = { onToggle() })
    }
}
