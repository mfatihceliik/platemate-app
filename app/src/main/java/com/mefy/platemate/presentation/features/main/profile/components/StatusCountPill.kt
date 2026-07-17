package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun StatusCountPill(
    label: String,
    value: String,
    dotColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(dims.radius.r16))
            .background(colors.surface, RoundedCornerShape(dims.radius.r16))
            .border(1.dp, colors.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(dims.radius.r16))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s8)
                .clip(CircleShape)
                .background(dotColor)
        )
        PMText(
            text = value,
            fontSize = dims.fontSize.lg,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )
        PMText(
            text = label,
            fontSize = dims.fontSize.sm,
            color = colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(name = "StatusCountPill Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun StatusCountPillLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        StatusCountPillPreviewContent()
    }
}

@Preview(name = "StatusCountPill Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun StatusCountPillDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        StatusCountPillPreviewContent()
    }
}

@Composable
private fun StatusCountPillPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    Row(
        modifier = Modifier.fillMaxWidth().padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        StatusCountPill(label = "Onaylanan", value = "124", dotColor = colors.success, modifier = Modifier.weight(1f))
        StatusCountPill(label = "Beklemede", value = "12", dotColor = colors.warning, modifier = Modifier.weight(1f))
        StatusCountPill(label = "Reddedilen", value = "6", dotColor = colors.error, modifier = Modifier.weight(1f))
    }
}