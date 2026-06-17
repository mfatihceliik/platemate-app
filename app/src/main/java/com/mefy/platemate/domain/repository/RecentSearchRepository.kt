package com.mefy.platemate.domain.repository

import com.mefy.platemate.domain.model.search.RecentSearch
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun observeRecent(): Flow<List<RecentSearch>>
    suspend fun upsertRecent(item: RecentSearch)
    suspend fun deleteRecent(normalizedPlateCode: String)
    suspend fun clearRecent()
}
