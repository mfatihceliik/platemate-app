package com.mefy.platemate.domain.model.discovery

import com.mefy.platemate.domain.model.plate.PlateDetail

/** Admin-managed Discover tab chip; `code` is the value sent as the tabType path segment. */
data class DiscoveryTabOption(
    val code: String,
    val label: String,
    val sortOrder: Int
)

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
