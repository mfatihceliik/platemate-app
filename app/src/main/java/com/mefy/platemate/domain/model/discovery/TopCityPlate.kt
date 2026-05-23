package com.mefy.platemate.domain.model.discovery

data class TopCityPlate(
    val plateCode: String,
    val todayReviewCount: Long,
    val todayReportCount: Long,
    val lastActivityAt: String,
    val ratingAverage: Double,
    val reviewCount: Long
)
