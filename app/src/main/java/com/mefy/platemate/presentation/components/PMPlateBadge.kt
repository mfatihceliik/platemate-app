package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.components.model.PlateBadgeSize
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMPlateBadge(
    cityCode: String,
    modifier: Modifier = Modifier,
    size: PlateBadgeSize = PlateBadgeSize.Medium
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val badgeSize: Dp
    val radius: Dp
    val stripeWidth: Dp
    val fontSize: TextUnit
    val paddingLeft: Dp

    when (size) {
        PlateBadgeSize.Small -> {
            badgeSize = dims.sizing.plateBadgeSmall
            radius = dims.radius.r8
            stripeWidth = dims.sizing.stripeWidthSmall
            fontSize = dims.fontSize.sm
            paddingLeft = 5.dp
        }
        PlateBadgeSize.Medium -> {
            badgeSize = dims.sizing.plateBadgeMedium
            radius = dims.radius.r12
            stripeWidth = dims.sizing.stripeWidthSmall
            fontSize = dims.fontSize.md
            paddingLeft = 5.dp
        }
        PlateBadgeSize.Large -> {
            badgeSize = dims.sizing.plateBadgeLarge
            radius = dims.radius.r16
            stripeWidth = dims.sizing.stripeWidthLarge
            fontSize = dims.fontSize.lg
            paddingLeft = 8.dp
        }
        PlateBadgeSize.Review -> {
            badgeSize = dims.sizing.plateBadgeReview
            radius = dims.radius.r12
            stripeWidth = dims.sizing.stripeWidthReview
            fontSize = dims.fontSize.md
            paddingLeft = 7.dp
        }
    }

    val shape = RoundedCornerShape(radius)

    Box(
        modifier = modifier
            .size(badgeSize)
            .clip(shape)
            .background(colors.primaryContainer)
            .border(dims.stroke.st1, colors.primaryContainerBorder, shape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(stripeWidth)
                .fillMaxHeight()
                .background(colors.primary)
        )
        PMText(
            text = cityCode,
            color = colors.onPrimaryContainer,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = paddingLeft)
        )
    }
}

@Preview(name = "PMPlateBadge Sizes", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMPlateBadgeSizesPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMPlateBadge(cityCode = "34", size = PlateBadgeSize.Small)
            PMPlateBadge(cityCode = "34", size = PlateBadgeSize.Medium)
            PMPlateBadge(cityCode = "34", size = PlateBadgeSize.Review)
            PMPlateBadge(cityCode = "34", size = PlateBadgeSize.Large)
        }
    }
}

@Preview(name = "PMPlateBadge Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMPlateBadgeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PMPlateBadge(cityCode = "06", size = PlateBadgeSize.Small)
            PMPlateBadge(cityCode = "06", size = PlateBadgeSize.Medium)
            PMPlateBadge(cityCode = "06", size = PlateBadgeSize.Review)
            PMPlateBadge(cityCode = "06", size = PlateBadgeSize.Large)
        }
    }
}
