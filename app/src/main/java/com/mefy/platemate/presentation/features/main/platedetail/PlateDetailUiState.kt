package com.mefy.platemate.presentation.features.main.platedetail

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.main.platedetail.model.CommentReportReason

@Immutable
data class PlateDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val plateCode: String = "",
    val cityCode: String = "",
    val cityName: String = "",
    val ratingAverage: Double = 0.0,
    val reviewCount: Long = 0,
    val ratingBreakdown: List<RatingBreakdownItem> = emptyList(),
    val tags: List<PlateTagUiModel> = emptyList(),
    val reviews: List<PlateReviewUiModel> = emptyList(),
    val reviewReport: ReviewReportUiState? = null
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
    val displayName: String?,
    val initials: String,
    val profilePhotoUrl: String?,
    val rating: Int,
    val timeAgo: String,
    val comment: String?,
    val reportTags: List<String>,
    val userId: Long = 0L,
    val canReport: Boolean = false,
    val canEdit: Boolean = false
)

@Immutable
data class ReviewReportUiState(
    val reviewId: Long,
    val reviewerName: String,
    val initials: String,
    val selectedReason: CommentReportReason? = null,
    val description: String = "",
    val isSubmitting: Boolean = false
)
