package com.mefy.platemate.presentation.features.main.profile

sealed interface ProfileUiAction {
    data class PlateReviewClicked(val normalizedPlateCode: String) : ProfileUiAction
    data object FriendsClicked : ProfileUiAction
    data object OnResume : ProfileUiAction
    data object RefreshRequested : ProfileUiAction
    data object RetryClicked : ProfileUiAction
}
