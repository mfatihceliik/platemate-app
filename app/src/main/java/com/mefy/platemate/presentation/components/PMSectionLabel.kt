package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun PMSectionLabel(
    modifier: Modifier = Modifier,
    color: Color = PMTheme.colors.textPrimary,
    style: PMTextStyle = PMTextStyle.SectionLabel,
    text: String
) {
    val spacing = PMTheme.spacing
    PMText(
        text = text,
        style = style,
        color = color,
        modifier = modifier.padding(
            start = spacing.s4,
            top = spacing.s4,
            bottom = spacing.s4
        )
    )
}