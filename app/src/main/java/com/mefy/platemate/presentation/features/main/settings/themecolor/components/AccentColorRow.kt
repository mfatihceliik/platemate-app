package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
internal fun AccentColorRow(
    modifier: Modifier = Modifier,
    rowColors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    gridSize: Int
) {
    val spacing = PMTheme.spacing

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.s12),
        modifier = modifier.fillMaxWidth()
    ) {
        rowColors.forEach { color ->
            AccentColorChip(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) },
                modifier = Modifier.weight(1f)
            )
        }

        val emptySpaces = gridSize - rowColors.size
        repeat(emptySpaces) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(name = "AccentColorRow Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AccentColorRowLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AccentColorRowPreviewContent()
    }
}

@Preview(name = "AccentColorRow Dark", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun AccentColorRowDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AccentColorRowPreviewContent()
    }
}

@Composable
private fun AccentColorRowPreviewContent() {
    var selectedColor by remember { mutableStateOf(AccentColors[0]) }
    val spacing = PMTheme.spacing

    AccentColorRow(
        rowColors = AccentColors.take(3), // 3 colors to show empty space behavior
        selectedColor = selectedColor,
        onColorSelected = { selectedColor = it },
        gridSize = 4,
        modifier = Modifier.padding(spacing.s16)
    )
}
