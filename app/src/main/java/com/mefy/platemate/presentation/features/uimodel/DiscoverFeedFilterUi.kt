package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class DiscoverFeedFilterUi(
    val cityIds: List<Int> = emptyList(),
    val cityNames: List<String> = emptyList(),
    val minRating: Int? = null,
    val reportTypeCode: String? = null,
    val reportTypeLabel: String? = null,
    val windowDays: Int? = null
) {
    val hasCityFilter: Boolean
        get() = cityIds.isNotEmpty()

    val hasActiveFilters: Boolean
        get() = hasCityFilter || minRating != null || reportTypeCode != null || windowDays != null

    // Sehir secimi kac sehir olursa olsun tek boyut sayilir; rozet sade kalir.
    val activeFilterCount: Int
        get() = (if (hasCityFilter) 1 else 0) +
            listOfNotNull(minRating, reportTypeCode, windowDays).size
}
