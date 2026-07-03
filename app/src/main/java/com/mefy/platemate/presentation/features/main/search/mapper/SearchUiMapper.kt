package com.mefy.platemate.presentation.features.main.search.mapper

import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel

interface SearchUiMapper {
    fun detectCityFromPlate(input: String): String?
    fun resolveCityName(cityName: String?, normalizedPlate: String): String?
    fun mapRecentSearch(
        result: PlateSearchResult,
        normalizedPlate: String,
        formattedPlate: String,
        detectedCityName: String?
    ): RecentSearch
    fun mapRecentSearchItem(item: RecentSearch, isBookmarked: Boolean): SearchRecentUiModel
    fun mapRecentSearches(items: List<RecentSearch>, bookmarkedCodes: Set<String>): List<SearchRecentUiModel>
    fun mapSavedPlates(items: List<SavedPlate>): List<SearchRecentUiModel>
    fun mapSavedPlate(item: RecentSearch): SavedPlate
    fun mapAlarmPlates(items: List<AlarmPlate>): List<SearchRecentUiModel>
}
