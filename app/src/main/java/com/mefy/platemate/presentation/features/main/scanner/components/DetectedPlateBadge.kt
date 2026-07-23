package com.mefy.platemate.presentation.features.main.scanner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun DetectedPlateBadge(
    modifier: Modifier = Modifier,
    plate: String
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val fontSize = PMTheme.fontSize
    val shapes = PMTheme.shapes

    Box(
        modifier = modifier
            .background(colors.scrim.copy(alpha = 0.6f), shape = shapes.medium)
            .padding(spacing.s16)
    ) {
        PMText(
            text = stringResource(R.string.scanner_searching_plate, plate),
            color = colors.success,
            fontSize = fontSize.lg,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "DetectedPlateBadge Light", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DetectedPlateBadgeLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        DetectedPlateBadge(plate = "34 ABC 123")
    }
}

@Preview(name = "DetectedPlateBadge Dark", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DetectedPlateBadgeDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        DetectedPlateBadge(plate = "34 ABC 123")
    }
}
