package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.plate.PlateDetailReviewItemDto
import com.mefy.platemate.data.remote.dto.plate.PlateSearchResponseDto
import com.mefy.platemate.data.remote.dto.plate.PlateTagSummaryItemDto
import com.mefy.platemate.data.remote.dto.plate.RatingDistributionItemDto
import com.mefy.platemate.domain.model.plate.PlateDetailReview
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.plate.PlateTagSummary
import com.mefy.platemate.domain.model.plate.RatingDistribution
import com.mefy.platemate.domain.model.report.ReportType
import javax.inject.Inject

class PlateSearchResultMapper @Inject constructor() :
    Mapper<PlateSearchResponseDto, PlateSearchResult> {

    override fun map(input: PlateSearchResponseDto): PlateSearchResult {
        val tagSummary = input.tagSummary?.map(::mapTagSummary) ?: emptyList()
        return PlateSearchResult(
            id = input.id ?: 0L,
            plateCode = input.plateCode.orEmpty(),
            cityName = input.cityName.orEmpty(),
            ratingAverage = input.ratingAverage ?: 0.0,
            reviewCount = input.reviewCount?.toLong() ?: 0L,
            ratingDistribution = input.ratingDistribution?.map(::mapRatingDistribution) ?: emptyList(),
            tagSummary = tagSummary,
            recentReviews = input.recentReviews?.map(::mapReview) ?: emptyList(),
            totalSearchCount = input.totalSearchCount ?: 0L,
            totalReviewCount = input.totalReviewCount ?: 0L,
            totalReportCount = input.totalReportCount ?: 0L,
            score = input.score ?: 0.0,
            lastActivityAt = input.lastActivityAt.orEmpty(),
            topReportTypes = tagSummary.map(::tagSummaryToReportType)
        )
    }

    private fun mapRatingDistribution(dto: RatingDistributionItemDto) = RatingDistribution(
        rating = dto.rating,
        count = dto.count,
        percentage = dto.percentage
    )

    private fun mapTagSummary(dto: PlateTagSummaryItemDto) = PlateTagSummary(
        code = dto.code,
        label = dto.label,
        iconKey = dto.iconKey,
        colorHex = dto.colorHex,
        count = dto.count
    )

    private fun mapReview(dto: PlateDetailReviewItemDto) = PlateDetailReview(
        id = dto.id,
        userId = dto.userId,
        username = dto.username.orEmpty(),
        displayName = dto.displayName,
        profilePhotoUrl = dto.profilePhotoUrl,
        rating = dto.rating,
        comment = dto.comment,
        reportTags = dto.reportTags ?: emptyList(),
        createdAt = dto.createdAt
    )

    private fun tagSummaryToReportType(tag: PlateTagSummary) = ReportType(
        code = tag.code,
        label = tag.label,
        description = "",
        iconKey = tag.iconKey,
        severity = "",
        colorHex = tag.colorHex,
        weight = 0,
        sortOrder = 0
    )
}
