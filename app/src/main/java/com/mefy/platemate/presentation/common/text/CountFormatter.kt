package com.mefy.platemate.presentation.common.text

import java.util.Locale

/**
 * Formats a count into a compact, human-friendly string (e.g. 1200 -> "1.2K").
 */
fun formatCompactCount(value: Long): String = when {
    value < 1_000 -> value.toString()
    value < 1_000_000 -> compact(value / 1_000.0, "K")
    else -> compact(value / 1_000_000.0, "M")
}

private fun compact(amount: Double, suffix: String): String {
    val rounded = if (amount % 1.0 == 0.0) {
        String.format(Locale.US, "%.0f", amount)
    } else {
        String.format(Locale.US, "%.1f", amount)
    }
    return rounded + suffix
}
