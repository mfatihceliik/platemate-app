package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.CommentReportReasonAdminDto
import com.mefy.platemate.domain.model.admin.CommentReportReasonAdmin
import javax.inject.Inject
import kotlin.text.orEmpty

class CommentReportReasonAdminMapper @Inject constructor() : Mapper<CommentReportReasonAdminDto, CommentReportReasonAdmin> {
    override fun map(input: CommentReportReasonAdminDto): CommentReportReasonAdmin = CommentReportReasonAdmin(
        id = input.id,
        code = input.code.orEmpty(),
        label = input.label.orEmpty(),
        requiresDescription = input.requiresDescription,
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}