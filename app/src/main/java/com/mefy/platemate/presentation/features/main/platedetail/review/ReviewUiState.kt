package com.mefy.platemate.presentation.features.main.platedetail.review

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

/**
 * Değerlendirme gönderiminin sonucu. Backend Success dönse de [Pending] olabilir
 * (status APPROVED değilse moderasyonda). Popup bu sonuca göre gösterilir.
 */
@Immutable
sealed interface ReviewSubmitResult {
    data object Success : ReviewSubmitResult
    data object Pending : ReviewSubmitResult
    data class Error(val message: UiText) : ReviewSubmitResult
}

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
