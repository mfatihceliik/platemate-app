package com.mefy.platemate.presentation.features.main.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
fun ChevronIcon() {
    PMIcon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        size = PMTheme.sizing.iconLg,
        tint = PMTheme.colors.textLabel
    )
}