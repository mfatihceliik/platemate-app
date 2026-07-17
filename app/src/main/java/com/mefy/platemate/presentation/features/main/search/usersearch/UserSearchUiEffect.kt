package com.mefy.platemate.presentation.features.main.search.usersearch

sealed interface UserSearchUiEffect {
    data class NavigateToUserProfile(val userId: Long) : UserSearchUiEffect
}
