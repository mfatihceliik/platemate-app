package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    
    val borderColor = when {
        !enabled -> colors.disabled
        selected -> colors.primary
        else -> colors.disabled
    }
    
    val backgroundColor = when {
        !enabled -> Color.Transparent
        selected -> colors.primary
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .size(dims.spacing.s24)
            .clip(CircleShape)
            .border(dims.stroke.st2, borderColor, CircleShape)
            .background(backgroundColor)
            .then(
                if (onClick != null && enabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(dims.spacing.s8)
                    .clip(CircleShape)
                    .background(if (enabled) Color.White else colors.disabled)
            )
        }
    }
}

@Preview(name = "PMRadioButton Default")
@Composable
private fun PMRadioButtonPreview() {
    PlateMateTheme {
        PMRadioButton(selected = true, onClick = {})
    }
}

@Preview(name = "PMRadioButton Unselected")
@Composable
private fun PMRadioButtonUnselectedPreview() {
    PlateMateTheme {
        PMRadioButton(selected = false, onClick = {})
    }
}
