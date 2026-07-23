package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PMStroke(
    val st1: Dp = 1.dp,
    val st2: Dp = 2.dp,
    val st3: Dp = 3.dp
)

val LocalStroke = staticCompositionLocalOf<PMStroke> {
    error("No Stroke Provided")
}