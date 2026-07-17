package com.mefy.platemate.presentation.features.main.search.usersearch

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.features.uimodel.UserSearchItemUiModel

@Immutable
data class UserSearchUiState(
    val searchQuery: String = "",
    val isSearchLoading: Boolean = false,
    val searchResults: List<UserSearchItemUiModel> = emptyList(),
    val recentSearches: List<UserSearchItemUiModel> = emptyList(),
    val isSearchActive: Boolean = true
)
