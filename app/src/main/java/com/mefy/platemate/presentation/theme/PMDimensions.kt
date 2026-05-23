package com.mefy.platemate.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class PMSpacing(
    val s0: Dp,
    val s2: Dp,
    val s4: Dp,
    val s6: Dp,
    val s8: Dp,
    val s10: Dp,
    val s12: Dp,
    val s14: Dp,
    val s16: Dp,
    val s18: Dp,
    val s20: Dp,
    val s22: Dp,
    val s24: Dp,
    val s32: Dp,
    val s36: Dp,
    val s40: Dp,
    val s48: Dp,
    val s52: Dp,
    val s56: Dp
)

@Immutable
data class PMRadius(
    val r8: Dp,
    val r10: Dp,
    val r12: Dp,
    val r14: Dp,
    val r18: Dp
)

@Immutable
data class PMStroke(
    val st1: Dp,
    val st2: Dp
)

@Immutable
data class PMDimensions(
    val spacing: PMSpacing,
    val radius: PMRadius,
    val stroke: PMStroke
)

val DefaultPMDimensions = PMDimensions(
    spacing = PMSpacing(
        s0 = 0.dp,
        s2 = 2.dp,
        s4 = 4.dp,
        s6 = 6.dp,
        s8 = 8.dp,
        s10 = 10.dp,
        s12 = 12.dp,
        s14 = 14.dp,
        s16 = 16.dp,
        s18 = 18.dp,
        s20 = 20.dp,
        s22 = 22.dp,
        s24 = 24.dp,
        s32 = 32.dp,
        s36 = 36.dp,
        s40 = 40.dp,
        s48 = 48.dp,
        s52 = 52.dp,
        s56 = 56.dp
    ),
    radius = PMRadius(
        r8 = 8.dp,
        r10 = 10.dp,
        r12 = 12.dp,
        r14 = 14.dp,
        r18 = 18.dp
    ),
    stroke = PMStroke(
        st1 = 1.dp,
        st2 = 2.dp
    )
)

val LocalPMDimensions = compositionLocalOf { DefaultPMDimensions }

val MaterialTheme.pmDimensions: PMDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalPMDimensions.current
