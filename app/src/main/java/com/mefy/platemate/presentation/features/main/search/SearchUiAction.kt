package com.mefy.platemate.presentation.features.main.search

sealed interface SearchUiAction {
    data class PlateInputChanged(val value: String) : SearchUiAction
    data object SearchClicked : SearchUiAction
    data class RecentItemClicked(val plateCode: String) : SearchUiAction
    data class RecentBookmarkClicked(val normalizedPlateCode: String) : SearchUiAction
    data class SavedPlateBookmarkClicked(val normalizedPlateCode: String) : SearchUiAction
    data class AlarmPlateRemoveClicked(val normalizedPlateCode: String) : SearchUiAction
    data class RecentDismissClicked(val normalizedPlateCode: String) : SearchUiAction
    data object ClearRecentClicked : SearchUiAction
}
