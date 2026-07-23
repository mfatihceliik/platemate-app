package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun RadioIndicator(
    modifier: Modifier = Modifier,
    isSelected: Boolean
) {
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val spacing = PMTheme.spacing
    val primary = colors.primary

    Box(
        modifier = modifier
            .size(spacing.s24)
            .clip(CircleShape)
            .border(stroke.st2, if (isSelected) primary else colors.disabled, CircleShape)
            .background(if (isSelected) primary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(spacing.s8)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}