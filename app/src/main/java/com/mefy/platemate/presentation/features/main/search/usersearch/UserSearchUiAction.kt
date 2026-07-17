package com.mefy.platemate.presentation.features.main.search.usersearch

sealed interface UserSearchUiAction {
    data class SearchQueryChanged(val query: String) : UserSearchUiAction
    data class SearchUserClicked(val userId: Long, val username: String) : UserSearchUiAction
    data class RecentSearchClicked(val userId: Long, val username: String) : UserSearchUiAction
    data object ClearRecentSearchesClicked : UserSearchUiAction
}
