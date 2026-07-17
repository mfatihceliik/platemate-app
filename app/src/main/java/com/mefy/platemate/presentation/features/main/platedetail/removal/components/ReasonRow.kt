package com.mefy.platemate.presentation.features.main.platedetail.removal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ReasonRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val shape = MaterialTheme.shapes.small

    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surfaceSecondary)
            .border(
                width = dims.stroke.st2,
                color = if (isSelected) colors.primary else colors.cardBorder,
                shape = shape
            )
            .debouncedClickable(onClick = onClick)
            .padding(dims.spacing.s12),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s24)
                .clip(CircleShape)
                .border(dims.stroke.st2, if (isSelected) colors.primary else colors.disabled, CircleShape)
                .background(if (isSelected) colors.primary else Color.Transparent),
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
        PMText(text = label, style = PMTextStyle.Body, color = colors.textPrimary, modifier = Modifier.fillMaxWidth())
    }
}