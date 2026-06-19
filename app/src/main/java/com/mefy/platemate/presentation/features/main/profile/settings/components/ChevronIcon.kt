package com.mefy.platemate.presentation.features.main.profile.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ChevronIcon() {
    PMIcon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        size = MaterialTheme.pmDimensions.sizing.iconLg,
        tint = MaterialTheme.pmColors.textLabel
    )
}