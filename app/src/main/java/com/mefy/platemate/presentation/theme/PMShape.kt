package com.mefy.platemate.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
@Immutable
data class PMShapes(
    val extraSmall: RoundedCornerShape = RoundedCornerShape(4.dp),
    val small: RoundedCornerShape = RoundedCornerShape(8.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(10.dp),
    val large: RoundedCornerShape = RoundedCornerShape(12.dp),
    val extraLarge: RoundedCornerShape = RoundedCornerShape(16.dp),
    val full: RoundedCornerShape = RoundedCornerShape(50)
)

val LocalShapes = staticCompositionLocalOf<PMShapes> {
    error("No Shape Provided")
}