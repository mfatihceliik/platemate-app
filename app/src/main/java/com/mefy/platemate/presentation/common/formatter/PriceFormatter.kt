package com.mefy.platemate.presentation.common.formatter

import java.util.Locale

/**
 * Formats a numeric amount + ISO currency code into a compact display price (e.g. 49.0 + "TRY" → "₺49").
 * The period suffix ("/mo", "/ay") is a localized string appended by the caller.
 */
object PriceFormatter {

    fun currencySymbol(currency: String): String = when (currency.uppercase(Locale.ROOT)) {
        "TRY" -> "₺"
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        else -> "${currency.uppercase(Locale.ROOT)} "
    }

    /** Whole amounts drop the decimals ("49"), fractional amounts keep two ("49.90"). */
    fun formatAmount(amount: Double): String =
        if (amount % 1.0 == 0.0) amount.toLong().toString()
        else String.format(Locale.getDefault(), "%.2f", amount)

    /** Symbol-prefixed price without the period suffix, e.g. "₺49". */
    fun price(amount: Double, currency: String): String =
        currencySymbol(currency) + formatAmount(amount)
}
