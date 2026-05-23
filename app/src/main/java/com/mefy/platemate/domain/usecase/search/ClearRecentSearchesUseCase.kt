package com.mefy.platemate.domain.usecase.search

import com.mefy.platemate.domain.repository.RecentSearchRepository
import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val repository: RecentSearchRepository
) {
    suspend operator fun invoke() {
        repository.clearRecent()
    }
}

