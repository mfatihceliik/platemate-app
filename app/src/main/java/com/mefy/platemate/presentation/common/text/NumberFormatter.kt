package com.mefy.platemate.presentation.common.text

import android.icu.text.CompactDecimalFormat
import java.util.Locale

/**
 * A centralized, static formatter object for number and metric formatting.
 * Uses ICU's CompactDecimalFormat to provide localized abbreviations (e.g. 1.2K in English, 1.2B in Turkish).
 */
object NumberFormatter {

    /**
     * Formats a count into a compact, human-friendly localized string.
     * Examples (English): 999 -> "999", 1500 -> "1.5K", 2000000 -> "2M"
     * Examples (Turkish): 999 -> "999", 1500 -> "1,5 B", 2000000 -> "2 Mn"
     */
    fun formatCompact(value: Long): String {
        val format = CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        )
        return format.format(value)
    }

    /**
     * Formats a rating value to consistently use 1 decimal place.
     * Uses the default locale to correctly format decimal separators (e.g., "4.8" or "4,8").
     */
    fun formatRating(value: Double): String {
        return String.format(Locale.getDefault(), "%.1f", value)
    }
}
