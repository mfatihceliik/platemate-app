package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMBadge(
    modifier: Modifier = Modifier,
    count: Int = 0,
    showCount: Boolean = true,
    minSize: Dp = 20.dp,
    dotSize: Dp = 8.dp,
    containerColor: Color = PMTheme.colors.primary,
    contentColor: Color = PMTheme.colors.onPrimary
) {
    val fontSize = PMTheme.fontSize
    val spacing = PMTheme.spacing

    if (showCount) {
        Box(
            modifier = modifier
                .sizeIn(minWidth = minSize, minHeight = minSize)
                .clip(CircleShape)
                .background(containerColor)
                .padding(horizontal = spacing.s4),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = if (count > 99) "99+" else count.toString(),
                color = contentColor,
                fontSize = fontSize.xs,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(dotSize)
                .clip(CircleShape)
                .background(containerColor)
        )
    }
}

@Preview(name = "PMBadge Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMBadgeLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMBadge(count = 3)
    }
}

@Preview(name = "PMBadge Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun PMBadgeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMBadge(count = 128)
    }
}
