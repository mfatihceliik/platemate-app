package com.mefy.platemate.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMRowDivider() {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions
    HorizontalDivider(
        thickness = dims.stroke.st1,
        color = colors.outlineVariant,
        modifier = Modifier.padding(horizontal = dims.spacing.s16)
    )
}