package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMSectionLabel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.pmColors.primary,
    style: PMTextStyle = PMTextStyle.SectionLabel,
    text: String
) {
    val dims = MaterialTheme.pmDimensions
    PMText(
        text = text,
        style = style,
        color = color,
        modifier = modifier.padding(
            start = dims.spacing.s4,
            top = dims.spacing.s8,
            bottom = dims.spacing.s4
        )
    )
}