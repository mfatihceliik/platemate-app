package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PMSpacing(
    val s0: Dp = 0.dp,
    val s4: Dp = 4.dp,
    val s8: Dp = 8.dp,
    val s10: Dp = 10.dp,
    val s12: Dp = 12.dp,
    val s16: Dp = 16.dp,
    val s24: Dp = 24.dp,
    val s32: Dp = 32.dp,
    val s48: Dp = 48.dp,
    val s64: Dp = 64.dp
)