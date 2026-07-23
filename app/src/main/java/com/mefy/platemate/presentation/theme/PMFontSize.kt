package com.mefy.platemate.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class PMFontSize(
    val xs: TextUnit = 10.sp,
    val sm: TextUnit = 12.sp,
    val md: TextUnit = 14.sp,
    val lg: TextUnit = 16.sp,
    val xl: TextUnit = 18.sp,
    val xxl: TextUnit = 26.sp,
    val huge: TextUnit = 36.sp
)

val LocalFontSize = staticCompositionLocalOf<PMFontSize> {
    error("No Font Size Provided")
}