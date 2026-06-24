package com.mefy.platemate.domain.model.plate

import com.mefy.platemate.domain.model.report.ReportType

data class PlateSearchResult(
    val id: Long,
    val plateCode: String,
    val cityName: String,
    val ratingAverage: Double,
    val reviewCount: Long,
    val ratingDistribution: List<RatingDistribution>,
    val tagSummary: List<PlateTagSummary>,
    val recentReviews: List<PlateDetailReview>,
    val totalSearchCount: Long,
    val totalReviewCount: Long,
    val totalReportCount: Long,
    val score: Double,
    val lastActivityAt: String,
    val topReportTypes: List<ReportType>
)
