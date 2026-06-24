package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun TagChipWithCount(label: String, count: Int) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    Box(
        modifier = Modifier
            .height(dims.sizing.chipHeight)
            .clip(RoundedCornerShape(dims.radius.rFull))
            .background(colors.surfaceVariant)
            .padding(horizontal = dims.spacing.s16),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMText(
                text = label,
                fontSize = dims.fontSize.md,
                fontWeight = FontWeight.SemiBold,
                color = colors.textSecondary
            )
            PMText(
                text = count.toString(),
                color = colors.textLabel
            )
        }
    }
}

@Preview(name = "TagChip Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TagChipWithCountLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        TagChipWithCountPreviewContent()
    }
}

@Preview(name = "TagChip Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun TagChipWithCountDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        TagChipWithCountPreviewContent()
    }
}

@Composable
private fun TagChipWithCountPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Row(
        modifier = Modifier.padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        TagChipWithCount(label = "Nazik", count = 42)
        TagChipWithCount(label = "Yol verdi", count = 31)
    }
}