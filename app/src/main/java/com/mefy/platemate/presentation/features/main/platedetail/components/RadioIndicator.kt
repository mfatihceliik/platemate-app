package com.mefy.platemate.presentation.features.main.platedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun RadioIndicator(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = colors.primary

    Box(
        modifier = modifier
            .size(dims.spacing.s24)
            .clip(CircleShape)
            .border(dims.stroke.st2, if (isSelected) primary else colors.disabled, CircleShape)
            .background(if (isSelected) primary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(dims.spacing.s8)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}