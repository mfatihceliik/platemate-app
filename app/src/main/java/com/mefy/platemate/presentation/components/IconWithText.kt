package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun IconWithText(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    color: Color
) {
    val dims = MaterialTheme.pmDimensions
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        PMIcon(
            imageVector = icon,
            tint = color,
            size = dims.sizing.iconSm
        )
        PMText(
            text = text,
            style = PMTextStyle.Caption,
            color = color
        )
    }
}