package com.mefy.platemate.data.local

import com.mefy.platemate.presentation.features.uimodel.UserSearchItemUiModel
import kotlinx.coroutines.flow.Flow

interface RecentUserSearchStore {
    val recentSearches: Flow<List<UserSearchItemUiModel>>
    suspend fun addSearchQuery(user: UserSearchItemUiModel)
    suspend fun removeSearch(id: Long)
    suspend fun updateCachedProfile(id: Long, displayName: String?, bio: String?)
    suspend fun clearSearches()
}
