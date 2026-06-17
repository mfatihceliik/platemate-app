package com.mefy.platemate.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

@Immutable
data class PMDimensions(
    val spacing: PMSpacing = PMSpacing(),
    val radius: PMRadius = PMRadius(),
    val stroke: PMStroke = PMStroke(),
    val sizing: PMSizing = PMSizing(),
    val fontSize: PMFontSize = PMFontSize(),
)

val DefaultPMDimensions = PMDimensions()

val LocalPMDimensions = compositionLocalOf { DefaultPMDimensions }

val MaterialTheme.pmDimensions: PMDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalPMDimensions.current
