package com.mefy.platemate.presentation.features.main.profile.reviewlist

sealed interface ProfileReviewListUiEffect {
    data object NavigateBack : ProfileReviewListUiEffect
    data class NavigateToReviewDetail(val plateCode: String, val reviewId: Long) : ProfileReviewListUiEffect
}
