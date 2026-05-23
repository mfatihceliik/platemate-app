package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.plate.PlateSearchRequest
import com.mefy.platemate.data.remote.dto.report.ReportTypeDto
import com.mefy.platemate.data.remote.dto.review.ReviewDto
import com.mefy.platemate.domain.model.plate.PlateSearchResult
import com.mefy.platemate.domain.model.report.ReportType
import com.mefy.platemate.domain.model.review.Review
import javax.inject.Inject

class PlateSearchResultMapper @Inject constructor() :
    Mapper<PlateSearchRequest, PlateSearchResult> {

    override fun map(input: PlateSearchRequest) = PlateSearchResult(
        id = input.id,
        plateCode = input.plateCode,
        cityName = input.cityName,
        ratingAverage = input.ratingAverage,
        totalRatingSum = input.totalRatingSum,
        totalSearchCount = input.totalSearchCount,
        totalReviewCount = input.totalReviewCount,
        totalReportCount = input.totalReportCount,
        totalWeightedReportScore = input.totalWeightedReportScore,
        score = input.score,
        lastActivityAt = input.lastActivityAt,
        recentReviews = mapRecentReviews(input.recentReviews),
        recentReportTypes = mapReportTypes(input.recentReportTypes)
    )

    private fun mapReportTypes(input: List<ReportTypeDto>): List<ReportType> {
        val result = input.map {
            mapReportType(it)
        }
        return result
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

    private fun mapRecentReviews(input: List<ReviewDto>): List<Review> {
        val result = input.map {
            mapRecentReview(it)
        }
        return result
    }

    private fun mapRecentReview(input: ReviewDto) = Review (
        id = input.id,
        plateCode = input.plateCode,
        rating = input.rating,
        comment = input.comment,
        userId = input.userId,
        reviewerUsername = input.reviewerUsername,
        createdAt = input.createdAt,
        updatedAt = input.updatedAt
    )

}
