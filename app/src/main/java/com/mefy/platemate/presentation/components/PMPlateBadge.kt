package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMPlateBadge(
    plate: String,
    modifier: Modifier = Modifier,
    size: Dp = MaterialTheme.pmDimensions.sizing.plateBadgeMd
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = RoundedCornerShape(dims.radius.r8)
    val bandWidth = size * 0.45f

    Row(
        modifier = modifier
            .height(size)
            .clip(shape)
            .background(colors.surface)
            .border(dims.stroke.st1, colors.surfaceVariant, shape)
    ) {
        Box(
            modifier = Modifier
                .width(bandWidth)
                .fillMaxHeight()
                .background(colors.plateBadge),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = "TR",
                color = colors.textWhite,
                fontSize = dims.fontSize.xs,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(top = dims.spacing.s8)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = dims.spacing.s8),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = plate,
                color = colors.textPrimary,
                fontSize = dims.fontSize.lg,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
            )
        }
    }
}

@Preview(name = "PMPlateBadge Sizes", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateBadgeSizesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMPlateBadge(plate = "34", size = 32.dp)
                PMPlateBadge(plate = "34", size = 48.dp)
            }
            PMPlateBadge(plate = "34 EK 0682", size = 32.dp)
            PMPlateBadge(plate = "34 EK 0682", size = 48.dp)
        }
    }
}

@Preview(name = "PMPlateBadge Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMPlateBadgeDarkPreview() {
    val dims = MaterialTheme.pmDimensions
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Column(
            modifier = Modifier.padding(dims.spacing.s16),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMPlateBadge(plate = "06", size = 48.dp)
            PMPlateBadge(plate = "06 ABC 123", size = 48.dp)
        }
    }
}
