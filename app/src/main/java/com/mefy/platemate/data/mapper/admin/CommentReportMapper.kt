package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.CommentReportDto
import com.mefy.platemate.domain.model.admin.CommentReport
import javax.inject.Inject

class CommentReportMapper @Inject constructor() : Mapper<CommentReportDto, CommentReport> {
    override fun map(input: CommentReportDto): CommentReport = CommentReport(
        id = input.id,
        commentId = input.commentId,
        plateCode = input.plateCode.orEmpty(),
        reasonCode = input.reasonCode.orEmpty(),
        description = input.description.orEmpty(),
        statusCode = input.statusCode.orEmpty(),
        createdAt = input.createdAt
    )
}