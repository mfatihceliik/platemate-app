package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.runtime.Immutable

@Immutable
data class PlateDetailUiState(
    val isLoading: Boolean = true,
    val plateCode: String = "",
    val cityCode: String = "",
    val cityName: String = "",
    val ratingAverage: Double = 0.0,
    val reviewCount: Long = 0,
    val ratingBreakdown: List<RatingBreakdownItem> = emptyList(),
    val tags: List<PlateTagUiModel> = emptyList(),
    val reviews: List<PlateReviewUiModel> = emptyList(),
    val isBookmarked: Boolean = false
) {
    val hasReviews: Boolean get() = reviews.isNotEmpty()
    val hasRatingBreakdown: Boolean get() = ratingBreakdown.isNotEmpty()
    val hasTags: Boolean get() = tags.isNotEmpty()
    val isEmpty: Boolean get() = !hasReviews && !hasTags && reviewCount == 0L
}

@Immutable
data class RatingBreakdownItem(
    val stars: Int,
    val percentage: Float
)

@Immutable
data class PlateTagUiModel(
    val code: String,
    val label: String,
    val count: Int
)

@Immutable
data class PlateReviewUiModel(
    val id: Long,
    val username: String,
    val initials: String,
    val rating: Int,
    val timeAgo: String,
    val comment: String
)
