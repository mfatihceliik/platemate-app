package com.mefy.platemate.presentation.features.main.profile

sealed interface ProfileUiAction {
    data class PlateReviewClicked(val normalizedPlateCode: String, val reviewId: Long) : ProfileUiAction
    data object FriendsClicked : ProfileUiAction
    data object FriendRequestActivityClicked : ProfileUiAction
    data class StatusSummaryClicked(val status: String) : ProfileUiAction
    data class ActivityTabChanged(val tabIndex: Int) : ProfileUiAction
    data object OnResume : ProfileUiAction
    data object RefreshRequested : ProfileUiAction
    data object RetryClicked : ProfileUiAction
}
