package com.mefy.platemate.presentation.features.main.profile

sealed interface ProfileUiAction {
    data class PlateReviewClicked(val normalizedPlateCode: String) : ProfileUiAction
    data object SettingsClicked : ProfileUiAction
    data object FriendsClicked : ProfileUiAction
    data object OnResume : ProfileUiAction
}
