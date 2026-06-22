package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.plate.PlateReviewDto
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.review.Review
import javax.inject.Inject

class PlateReviewMapper @Inject constructor() : Mapper<PlateReviewDto, Review> {

    override fun map(input: PlateReviewDto): Review = Review(
        id = input.id,
        plateCode = input.plateCode,
        rating = input.rating,
        comment = input.comment,
        reviewStatus = input.reviewStatusCode,
        userId = input.userId,
        reviewerUsername = input.username.orEmpty(),
        createdAt = input.createdAt.toAppDateTimeOrNull(),
        updatedAt = input.updatedAt.toAppDateTimeOrNull()
    )

    private fun String?.toAppDateTimeOrNull(): AppDateTime? =
        this?.takeIf { it.isNotBlank() }?.let { AppDateTime(it) }
}
