package com.mefy.platemate.domain.model.plate

import com.mefy.platemate.domain.model.report.ReportType

data class PlateDetail(
    val plateCode: String,
    val cityName: String?,
    val ratingAverage: Double,
    val reviewCount: Long,
    val todaySearchCount: Long,
    val todayReviewCount: Long,
    val todayReportCount: Long,
    val todayWeightedReportScore: Double,
    val score: Double,
    val lastActivityAt: String,
    val topReportType: List<ReportType>
)
