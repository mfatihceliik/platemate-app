package com.mefy.platemate.presentation.features.uimodel

import androidx.annotation.StringRes
import com.mefy.platemate.R

/**
 * Bilinen 4 Discover tab kodu icin lokalize label fallback'i. Sekmelerin kendisi artik
 * backend'den (`GET /api/discovery/tabs`) dinamik geliyor; bu enum yalnizca bu kodlar icin
 * client-side string kaynagi saglar (bilinmeyen bir kod icin backend'in duz label'i kullanilir).
 */
enum class DiscoverFilterUi(
    val code: String,
    @param:StringRes val labelRes: Int
) {
    Trend("TREND", R.string.discover_filter_trend),
    Careless("DANGEROUS", R.string.discover_filter_careless),
    GoodDriver("GOOD_DRIVER", R.string.discover_filter_good_driver),
    Newest("NEW", R.string.discover_filter_new);

    companion object {
        fun labelResFor(code: String): Int? = entries.find { it.code == code }?.labelRes
    }
}
