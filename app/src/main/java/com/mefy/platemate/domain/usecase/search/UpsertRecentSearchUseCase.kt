package com.mefy.platemate.domain.usecase.search

import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.repository.RecentSearchRepository
import javax.inject.Inject

class UpsertRecentSearchUseCase @Inject constructor(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke(item: RecentSearch) {
        repository.upsertRecent(item)
    }
}

