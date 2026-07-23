package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PMRadius(
    val r4: Dp = 4.dp,
    val r6: Dp = 6.dp,
    val r8: Dp = 8.dp,
    val r10: Dp = 10.dp,
    val r12: Dp = 12.dp,
    val r16: Dp = 16.dp,
    val r18: Dp = 18.dp,
    val r24: Dp = 24.dp,
    val rFull: Dp = 999.dp
)

val LocalRadius = staticCompositionLocalOf<PMRadius> {
    error("No Radius Provided")
}