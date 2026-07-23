package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.features.main.settings.themecolor.AccentColors
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun AccentColorGrid(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
    gridSize: Int = 4
) {
    val spacing = PMTheme.spacing

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.s12),
        modifier = modifier.fillMaxWidth()
    ) {
        colors.chunked(gridSize.coerceAtLeast(1)).forEach { rowColors ->
            AccentColorRow(
                rowColors = rowColors,
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
                gridSize = gridSize
            )
        }
    }
}

@Preview(name = "AccentColorGrid Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccentColorGridLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AccentColorGridPreviewContent()
    }
}

@Preview(name = "AccentColorGrid Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun AccentColorGridDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AccentColorGridPreviewContent()
    }
}

@Composable
private fun AccentColorGridPreviewContent() {
    val spacing = PMTheme.spacing
    var selectedColor by remember { mutableStateOf(AccentColors[0]) }

    AccentColorGrid(
        colors = AccentColors,
        selectedColor = selectedColor,
        onColorSelected = { selectedColor = it },
        modifier = Modifier.padding(spacing.s16)
    )
}
