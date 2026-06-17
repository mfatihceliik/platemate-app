package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.plate.PlateDetailDto
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import javax.inject.Inject

class PlateSearchResultMapper @Inject constructor() :
    Mapper<PlateDetailDto, PlateSearchResult> {

    override fun map(input: PlateDetailDto) = PlateSearchResult(
        plateCode = input.plateCode.orEmpty(),
        cityName = input.cityName.orEmpty(),
        ratingAverage = input.ratingAverage,
        reviewCount = input.reviewCount,
        todaySearchCount = input.todaySearchCount,
        todayReviewCount = input.todayReviewCount,
        todayReportCount = input.todayReportCount,
        todayWeightedReportScore = input.todayWeightedReportScore,
        score = input.score,
        lastActivityAt = input.lastActivityAt.orEmpty(),
        topReportTypes = mapReportTypes(input.reportTypeDto)
    )

    private fun mapReportTypes(input: List<ReportTypeDto>?): List<ReportType> {
        if (input == null) return emptyList()
        return input.map {
            mapReportType(it)
        }
    }

    private fun mapReportType(input: ReportTypeDto) = ReportType (
        code = input.code,
        label = input.label,
        description = input.description,
        iconKey = input.iconKey,
        severity = input.severity,
        colorHex = input.colorHex,
        weight = input.weight,
        sortOrder = input.sortOrder
    )

}
