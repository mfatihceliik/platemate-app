package com.mefy.platemate.domain.usecase.search

import com.mefy.platemate.domain.repository.RecentSearchRepository
import javax.inject.Inject

class ObserveRecentSearchesUseCase @Inject constructor(
    private val repository: RecentSearchRepository
) {
    operator fun invoke() = repository.observeRecent()
}

