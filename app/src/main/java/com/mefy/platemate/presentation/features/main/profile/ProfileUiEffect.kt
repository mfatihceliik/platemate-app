package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.presentation.common.text.UiText

sealed interface ProfileUiEffect {
    data class NavigateToReviewDetail(val normalizedPlateCode: String, val reviewId: Long) : ProfileUiEffect
    data class NavigateToFriends(val initialTab: Int = 0) : ProfileUiEffect
    data class ShowSnackbar(val message: UiText) : ProfileUiEffect
    data class NavigateToUserProfile(val userId: Long) : ProfileUiEffect
    data class NavigateToReviewList(val status: String) : ProfileUiEffect
}
