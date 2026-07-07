package com.mefy.platemate.presentation.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Locale

/** `#RRGGBB` → opaque [Color]; falls back to the default teal on a malformed hex. */
fun hexToColor(hex: String): Color {
    val cleaned = hex.trim().removePrefix("#")
    val rgb = cleaned.toLongOrNull(16)
    return if (cleaned.length == 6 && rgb != null) Color(0xFF000000 or rgb) else Color(0xFF06B6D4)
}

/** [Color] → `#RRGGBB` (uppercase, alpha dropped). */
fun Color.toHex(): String = "#%06X".format(Locale.ROOT, toArgb() and 0xFFFFFF)
