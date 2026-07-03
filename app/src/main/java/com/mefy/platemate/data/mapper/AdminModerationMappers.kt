package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.CommentReportDto
import com.mefy.platemate.data.remote.dto.admin.PlateAdminDto
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalRequestDto
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeAdminDto
import com.mefy.platemate.data.remote.dto.admin.SocialPlatformAdminDto
import com.mefy.platemate.domain.model.admin.CommentReport
import com.mefy.platemate.domain.model.admin.HiddenPlate
import com.mefy.platemate.domain.model.admin.PlateRemovalRequest
import com.mefy.platemate.domain.model.admin.ReportTypeAdmin
import com.mefy.platemate.domain.model.admin.SocialPlatformAdmin
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

class PlateRemovalRequestMapper @Inject constructor() : Mapper<PlateRemovalRequestDto, PlateRemovalRequest> {
    override fun map(input: PlateRemovalRequestDto): PlateRemovalRequest = PlateRemovalRequest(
        id = input.id,
        plateCode = input.plateCode.orEmpty(),
        requesterUsername = input.requesterUsername.orEmpty(),
        requesterEmail = input.requesterEmail.orEmpty(),
        reasonCode = input.reasonCode.orEmpty(),
        description = input.description.orEmpty(),
        statusCode = input.statusCode.orEmpty(),
        createdAt = input.createdAt
    )
}

class HiddenPlateMapper @Inject constructor() : Mapper<PlateAdminDto, HiddenPlate> {
    override fun map(input: PlateAdminDto): HiddenPlate = HiddenPlate(
        id = input.id,
        plateCode = input.plateCode.orEmpty(),
        statusCode = input.statusCode.orEmpty(),
        hiddenReason = input.hiddenReason.orEmpty(),
        reviewCount = input.reviewCount ?: 0,
        reportCount = input.reportCount ?: 0,
        updatedAt = input.updatedAt
    )
}

class ReportTypeAdminMapper @Inject constructor() : Mapper<PlateReportTypeAdminDto, ReportTypeAdmin> {
    override fun map(input: PlateReportTypeAdminDto): ReportTypeAdmin = ReportTypeAdmin(
        id = input.id,
        code = input.code.orEmpty(),
        label = input.label.orEmpty(),
        description = input.description.orEmpty(),
        iconKey = input.iconKey.orEmpty(),
        severityCode = input.severityCode.orEmpty(),
        colorHex = input.colorHex.orEmpty(),
        weight = input.weight ?: 0,
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}

class SocialPlatformAdminMapper @Inject constructor() : Mapper<SocialPlatformAdminDto, SocialPlatformAdmin> {
    override fun map(input: SocialPlatformAdminDto): SocialPlatformAdmin = SocialPlatformAdmin(
        id = input.id,
        code = input.code.orEmpty(),
        label = input.label.orEmpty(),
        iconUrl = input.iconUrl.orEmpty(),
        backgroundColorHex = input.backgroundColorHex.orEmpty(),
        iconTintColorHex = input.iconTintColorHex.orEmpty(),
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}
