package com.mefy.platemate.domain.model.search

import com.mefy.platemate.domain.model.report.ReportType

data class SavedPlate(
    val normalizedPlateCode: String,
    val formattedPlateCode: String,
    val cityName: String?,
    val ratingAverage: Double,
    val commentCount: Long,
    val reportTypes: List<ReportType>,
    val savedAt: Long
)
