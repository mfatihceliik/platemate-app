package com.mefy.platemate.presentation.features.main.profile.reviewlist

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class ProfileReviewListItemUiModel(
    val id: Long,
    val plateCode: String,
    val comment: String,
    val rating: Int,
    val dateText: String
)

@Immutable
data class ProfileReviewListUiState(
    val selectedTab: Int = 0,
    val searchQuery: String = "",
    val isInitialLoading: Boolean = true,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: UiText? = null,
    val reviews: List<ProfileReviewListItemUiModel> = emptyList(),
    val page: Int = 0,
    val endReached: Boolean = false
) {
    val isEmpty: Boolean get() = !isLoading && !isInitialLoading && errorMessage == null && reviews.isEmpty()
}
