package com.mefy.platemate.domain.model.plate

import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review

data class PlateSearchResult(
    val id: Long,
    val plateCode: String,
    val cityName: String,
    val ratingAverage: Double,
    val totalRatingSum: Long,
    val totalSearchCount: Long,
    val totalReviewCount: Long,
    val totalReportCount: Long,
    val totalWeightedReportScore: Long,
    val score: Int,
    val lastActivityAt: String,
    val recentReviews: List<Review>,
    val recentReportTypes: List<ReportType>,
)
