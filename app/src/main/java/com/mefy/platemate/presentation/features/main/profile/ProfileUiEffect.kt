package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.presentation.common.text.UiText

sealed interface ProfileUiEffect {
    data class NavigateToSearchDetail(val normalizedPlateCode: String) : ProfileUiEffect
    data object NavigateToFriends : ProfileUiEffect
    data class ShowSnackbar(val message: UiText) : ProfileUiEffect
}
