package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.compose.runtime.Immutable

@Immutable
data class ReviewUiState(
    val plateCode: String = "",
    val cityCode: String = "",
    val cityName: String = "",
    val reviewCount: Long = 0,
    val overallRating: Int = 0,
    val tags: List<ReviewTagUiModel> = emptyList(),
    val comment: String = "",
    val isAnonymous: Boolean = false,
    val isSubmitting: Boolean = false,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val submitResult: ReviewSubmitResult? = null
) {
    companion object {
        const val REVIEW_COMMENT_MAX_LENGTH = 240
    }

    val isSubmitEnabled: Boolean
        get() = overallRating > 0 && !isSubmitting && comment.length <= REVIEW_COMMENT_MAX_LENGTH
}

@Immutable
data class ReviewTagUiModel(
    val code: String,
    val label: String,
    val isSelected: Boolean = false
)
