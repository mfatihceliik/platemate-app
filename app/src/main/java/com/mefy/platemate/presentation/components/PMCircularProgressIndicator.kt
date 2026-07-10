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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun PMCircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = MaterialTheme.pmDimensions.sizing.circleProgressBarSm,
    strokeWidth: Dp = MaterialTheme.pmDimensions.stroke.st2,
    color: Color = MaterialTheme.pmColors.primary,
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
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.pmColors.background)
            .padding(dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
    ) {
        PMCircularProgressIndicator(
            size = dims.sizing.circleProgressBarXs,
            strokeWidth = dims.stroke.st2
        )
        PMCircularProgressIndicator(
            size = dims.sizing.circleProgressBarSm,
            strokeWidth = dims.stroke.st2
        )
        PMCircularProgressIndicator(
            size = dims.sizing.circleProgressBarMd,
            strokeWidth = dims.stroke.st2
        )
        PMCircularProgressIndicator(
            size = dims.sizing.circleProgressBarLg,
            strokeWidth = dims.stroke.st2
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
