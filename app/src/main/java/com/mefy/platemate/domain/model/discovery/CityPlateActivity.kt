package com.mefy.platemate.domain.model.discovery

data class CityPlateActivity(
    val plateCode: String,
    val todayReviewCount: Long,
    val todayReportCount: Long,
    val lastActivityAt: String,
    val ratingAverage: Double,
    val reviewCount: Long
)

data class CityPlatePage(
    val items: List<CityPlateActivity>,
    val page: Int,
    val hasNext: Boolean
)
