package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.data.remote.dto.plate.PlateReviewDto
import com.mefy.platemate.core.common.pagination.PaginationMeta
import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.domain.model.review.Review
import javax.inject.Inject

class PlateReviewPageMapper @Inject constructor(
    private val plateReviewMapper: PlateReviewMapper
) : Mapper<PagedResult<PlateReviewDto>, PagedResult<Review>> {
    override fun map(input: PagedResult<PlateReviewDto>): PagedResult<Review> = PagedResult(
        items = plateReviewMapper.mapList(input.items),
        meta = PaginationMeta(
            page = input.meta.page,
            size = input.meta.size,
            totalElements = input.meta.totalElements,
            totalPages = input.meta.totalPages,
            hasNext = input.meta.hasNext,
            hasPrevious = input.meta.hasPrevious
        )
    )
}
