package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.data.remote.dto.review.ReviewDto

data class PlateSearchRequest(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("ratingAverage") val ratingAverage: Double,
    @SerializedName("totalRatingSum") val totalRatingSum: Long,
    @SerializedName("totalSearchCount") val totalSearchCount: Long,
    @SerializedName("totalReviewCount") val totalReviewCount: Long,
    @SerializedName("totalReportCount") val totalReportCount: Long,
    @SerializedName("totalWeightedReportScore") val totalWeightedReportScore: Long,
    @SerializedName("score") val score: Int,
    @SerializedName("lastActivityAt") val lastActivityAt: String,
    @SerializedName("recentReviews") val recentReviews: List<ReviewDto>,
    @SerializedName("recentReportTypes") val recentReportTypes: List<ReportTypeDto>,
)
