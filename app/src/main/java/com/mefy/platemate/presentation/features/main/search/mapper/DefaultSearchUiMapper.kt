package com.mefy.platemate.presentation.features.main.search.mapper

import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.search.AlarmPlate
import com.mefy.platemate.domain.model.search.RecentSearch
import com.mefy.platemate.domain.model.search.SavedPlate
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.features.main.search.model.SearchRecentUiModel
import com.mefy.platemate.presentation.features.uimodel.PlateReportTagUiModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSearchUiMapper @Inject constructor() : SearchUiMapper {

    override fun detectCityFromPlate(input: String): String? =
        CityNameResolver.detectCityFromPlate(input)

    override fun resolveCityName(cityName: String?, normalizedPlate: String): String? =
        CityNameResolver.resolveCityName(cityName = cityName, plateCode = normalizedPlate)

    override fun mapRecentSearch(
        result: PlateSearchResult,
        normalizedPlate: String,
        formattedPlate: String,
        detectedCityName: String?
    ): RecentSearch = RecentSearch(
        normalizedPlateCode = normalizedPlate,
        formattedPlateCode = formattedPlate,
        cityName = detectedCityName,
        reportTypes = result.topReportTypes,
        ratingAverage = result.ratingAverage,
        commentCount = result.reviewCount
    )

    override fun mapRecentSearchItem(item: RecentSearch, isBookmarked: Boolean): SearchRecentUiModel =
        SearchRecentUiModel(
            normalizedPlateCode = item.normalizedPlateCode,
            plateCode = item.formattedPlateCode,
            cityName = item.cityName,
            reportTags = mapReportTags(item.reportTypes),
            ratingAverage = item.ratingAverage,
            commentCount = item.commentCount,
            isBookmarked = isBookmarked
        )

    override fun mapRecentSearches(
        items: List<RecentSearch>,
        bookmarkedCodes: Set<String>
    ): List<SearchRecentUiModel> = items.map { item ->
        mapRecentSearchItem(item, isBookmarked = bookmarkedCodes.contains(item.normalizedPlateCode))
    }

    override fun mapSavedPlates(items: List<SavedPlate>): List<SearchRecentUiModel> = items.map { item ->
        SearchRecentUiModel(
            normalizedPlateCode = item.normalizedPlateCode,
            plateCode = item.formattedPlateCode,
            cityName = resolveCityName(item.cityName, item.normalizedPlateCode),
            reportTags = mapReportTags(item.reportTypes),
            ratingAverage = item.ratingAverage,
            commentCount = item.commentCount,
            isBookmarked = true
        )
    }

    override fun mapAlarmPlates(items: List<AlarmPlate>): List<SearchRecentUiModel> = items.map { item ->
        SearchRecentUiModel(
            normalizedPlateCode = item.normalizedPlateCode,
            plateCode = item.formattedPlateCode,
            cityName = resolveCityName(item.cityName, item.normalizedPlateCode),
            reportTags = mapReportTags(item.reportTypes),
            ratingAverage = item.ratingAverage,
            commentCount = item.commentCount,
            isBookmarked = false
        )
    }

    override fun mapSavedPlate(item: RecentSearch): SavedPlate = SavedPlate(
        normalizedPlateCode = item.normalizedPlateCode,
        formattedPlateCode = item.formattedPlateCode,
        cityName = item.cityName,
        ratingAverage = item.ratingAverage,
        commentCount = item.commentCount,
        reportTypes = item.reportTypes,
        savedAt = System.currentTimeMillis()
    )

    private fun mapReportTags(reportTypes: List<ReportType>): List<PlateReportTagUiModel> =
        reportTypes.map { reportType ->
            PlateReportTagUiModel(
                code = reportType.code,
                label = reportType.label,
                severity = reportType.severity,
                colorHex = reportType.colorHex
            )
        }
}
