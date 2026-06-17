package com.mefy.platemate.data.repository

import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.repository.RecentSearchRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class InMemoryRecentSearchRepository @Inject constructor() : RecentSearchRepository {

    private val recent = MutableStateFlow<List<RecentSearch>>(emptyList())

    override fun observeRecent(): Flow<List<RecentSearch>> = recent.asStateFlow()

    override suspend fun upsertRecent(item: RecentSearch) {
        recent.update { current ->
            buildList {
                add(item)
                addAll(
                    current.filterNot { existing ->
                        existing.normalizedPlateCode == item.normalizedPlateCode
                    }
                )
            }.take(MAX_RECENT_ITEMS)
        }
    }

    override suspend fun deleteRecent(normalizedPlateCode: String) {
        recent.update { current ->
            current.filterNot { item -> item.normalizedPlateCode == normalizedPlateCode }
        }
    }

    override suspend fun clearRecent() {
        recent.value = emptyList()
    }

    private companion object {
        const val MAX_RECENT_ITEMS = 10
    }
}
