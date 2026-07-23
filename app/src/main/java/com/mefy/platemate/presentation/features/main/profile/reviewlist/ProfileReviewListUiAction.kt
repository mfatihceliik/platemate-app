package com.mefy.platemate.presentation.features.main.profile.reviewlist

sealed interface ProfileReviewListUiAction {
    data class TabChanged(val index: Int) : ProfileReviewListUiAction
    data class SearchQueryChanged(val query: String) : ProfileReviewListUiAction
    data object LoadMoreRequested : ProfileReviewListUiAction
    data class ReviewClicked(val plateCode: String, val reviewId: Long) : ProfileReviewListUiAction
    data object BackClicked : ProfileReviewListUiAction
}
