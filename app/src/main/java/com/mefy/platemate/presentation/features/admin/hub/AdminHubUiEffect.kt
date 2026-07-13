package com.mefy.platemate.presentation.features.admin.hub

sealed interface AdminHubUiEffect {
    data object NavigateBack : AdminHubUiEffect
    data class NavigateToItem(val code: String) : AdminHubUiEffect
}
