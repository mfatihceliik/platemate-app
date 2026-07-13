package com.mefy.platemate.presentation.features.admin.hub

sealed interface AdminHubUiAction {
    data object BackClicked : AdminHubUiAction
    data object RetryClicked : AdminHubUiAction
    data class QueryChanged(val value: String) : AdminHubUiAction
    data class ItemClicked(val code: String) : AdminHubUiAction
}
