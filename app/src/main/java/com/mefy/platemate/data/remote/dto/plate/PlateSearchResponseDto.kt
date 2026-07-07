package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class PlateSearchResponseDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("cityName") val cityName: String?,
    @SerializedName("ratingAverage") val ratingAverage: Double?,
    @SerializedName("reviewCount") val reviewCount: Int?,
    @SerializedName("totalRatingSum") val totalRatingSum: Long?,
    @SerializedName("ratingDistribution") val ratingDistribution: List<RatingDistributionItemDto>?,
    @SerializedName("tagSummary") val tagSummary: List<PlateTagSummaryItemDto>?,
    @SerializedName("totalSearchCount") val totalSearchCount: Long?,
    @SerializedName("totalReviewCount") val totalReviewCount: Long?,
    @SerializedName("totalReportCount") val totalReportCount: Long?,
    @SerializedName("totalWeightedReportScore") val totalWeightedReportScore: Long?,
    @SerializedName("score") val score: Double?,
    @SerializedName("lastActivityAt") val lastActivityAt: String?,
    @SerializedName("recentReviews") val recentReviews: List<PlateDetailReviewItemDto>?,
    @SerializedName("following") val following: Boolean?
)
