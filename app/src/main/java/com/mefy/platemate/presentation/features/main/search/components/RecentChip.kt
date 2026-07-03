package com.mefy.platemate.presentation.features.main.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun RecentChip(
    plateCode: String,
    onClick: (String) -> Unit
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    val shape = remember(dims.radius.r12) { RoundedCornerShape(dims.radius.r12) }

    Box(
        modifier = Modifier
            .height(dims.sizing.chipHeight)
            .clip(shape)
            .border(dims.stroke.st1, colors.primary, shape)
            .background(colors.surface)
            .debouncedClickable { onClick(plateCode) }
            .padding(horizontal = dims.spacing.s16),
        contentAlignment = Alignment.Center
    ) {
        PMText(
            text = plateCode,
            fontSize = dims.fontSize.md,
            fontWeight = FontWeight.SemiBold,
            color = colors.textSecondary
        )
    }
}

@Preview(name = "RecentChip Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun RecentChipLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        RecentChipPreviewContent()
    }
}

@Preview(name = "RecentChip Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun RecentChipDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        RecentChipPreviewContent()
    }
}

@Composable
private fun RecentChipPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        RecentChip(plateCode = "34 EK 0682", onClick = {})
        RecentChip(plateCode = "06 ABC 123", onClick = {})
        RecentChip(plateCode = "35 XY 99", onClick = {})
    }
}