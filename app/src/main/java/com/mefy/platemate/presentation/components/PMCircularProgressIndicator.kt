package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.LocalPMDimensions
import com.mefy.platemate.presentation.theme.PMDimensions
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMCircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = MaterialTheme.pmDimensions.sizing.progressBarSmall,
    strokeWidth: Dp = MaterialTheme.pmDimensions.stroke.st2,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = Color.Transparent,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = strokeWidth,
        trackColor = trackColor,
    )
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
    ) {
        // Small — inline / button use
        PMCircularProgressIndicator(size = dims.sizing.iconMd, strokeWidth = 2.dp)

        // Default — list / card loading
        PMCircularProgressIndicator()

        // Large — full-screen loading overlay
        PMCircularProgressIndicator(size = 48.dp, strokeWidth = 4.dp)

        // Custom color
        PMCircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
    }
}
