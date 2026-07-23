package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMCircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = PMTheme.sizing.circleProgressBarSm,
    strokeWidth: Dp = PMTheme.stroke.st2,
    color: Color = PMTheme.colors.primary,
    trackColor: Color = Color.Transparent,
    fillMaxSize: Boolean = false,
) {
    if (fillMaxSize) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                color = color,
                strokeWidth = strokeWidth,
                trackColor = trackColor,
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = modifier.size(size),
            color = color,
            strokeWidth = strokeWidth,
            trackColor = trackColor,
        )
    }
}

@Preview(name = "PMCircularProgressIndicator Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMCircularProgressIndicatorLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMCircularProgressIndicatorPreviewContent()
    }
}

@Preview(name = "PMCircularProgressIndicator Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMCircularProgressIndicatorDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMCircularProgressIndicatorPreviewContent()
    }
}

@Composable
private fun PMCircularProgressIndicatorPreviewContent() {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s24)
    ) {
        PMCircularProgressIndicator(
            size = sizing.circleProgressBarXs,
            strokeWidth = stroke.st2
        )
        PMCircularProgressIndicator(
            size = sizing.circleProgressBarSm,
            strokeWidth = stroke.st2
        )
        PMCircularProgressIndicator(
            size = sizing.circleProgressBarMd,
            strokeWidth = stroke.st2
        )
        PMCircularProgressIndicator(
            size = sizing.circleProgressBarLg,
            strokeWidth = stroke.st2
        )
    }
}

@Preview(name = "PMCircularProgressIndicator FullScreen", showBackground = true, backgroundColor = 0xFFF6F8FB, heightDp = 240)
@Composable
private fun PMCircularProgressIndicatorFullScreenPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMCircularProgressIndicator(fillMaxSize = true)
    }
}
