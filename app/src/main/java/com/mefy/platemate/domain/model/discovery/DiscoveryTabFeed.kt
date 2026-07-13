package com.mefy.platemate.domain.model.discovery

import com.mefy.platemate.domain.model.plate.PlateDetail

enum class DiscoveryTabType {
    TREND,
    DANGEROUS,
    GOOD_DRIVER,
    NEW
}

data class DiscoveryTabFilter(
    val cityIds: List<Int> = emptyList(),
    val reportTypeCode: String? = null,
    val minRating: Double? = null,
    val windowDays: Int? = null
) {
    val hasActiveFilters: Boolean
        get() = cityIds.isNotEmpty() || reportTypeCode != null || minRating != null || windowDays != null
}

data class DiscoveryTabPage(
    val items: List<PlateDetail>,
    val page: Int,
    val hasNext: Boolean
)
