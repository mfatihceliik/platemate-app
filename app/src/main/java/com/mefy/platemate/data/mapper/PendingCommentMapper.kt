package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PlateReviewAdminDto
import com.mefy.platemate.domain.model.admin.PendingComment
import javax.inject.Inject

class PendingCommentMapper @Inject constructor() : Mapper<PlateReviewAdminDto, PendingComment> {
    override fun map(input: PlateReviewAdminDto): PendingComment = PendingComment(
        id = input.id,
        plateCode = input.plateCode.orEmpty(),
        username = input.username.orEmpty(),
        rating = input.rating ?: 0,
        comment = input.comment.orEmpty(),
        reportCount = input.reportCount ?: 0,
        reportTags = input.reportTags.orEmpty(),
        createdAt = input.createdAt
    )
}
