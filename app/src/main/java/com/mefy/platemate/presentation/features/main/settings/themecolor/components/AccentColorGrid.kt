package com.mefy.platemate.presentation.features.main.settings.themecolor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AccentColorGrid(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
    gridSize: Int = 4
) {
    val dims = MaterialTheme.pmDimensions

    Column(
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        modifier = modifier.fillMaxWidth()
    ) {
        colors.chunked(gridSize.coerceAtLeast(1)).forEach { rowColors ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowColors.forEach { color ->
                    AccentColorChip(
                        color = color,
                        isSelected = color == selectedColor,
                        onClick = { onColorSelected(color) },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Fill empty spaces to maintain alignment
                val emptySpaces = gridSize - rowColors.size
                repeat(emptySpaces) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
