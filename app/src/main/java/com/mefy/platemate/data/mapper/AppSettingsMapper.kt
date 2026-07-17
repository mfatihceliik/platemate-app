package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.AppSettingsDto
import com.mefy.platemate.domain.model.admin.AppSettings
import javax.inject.Inject

class AppSettingsMapper @Inject constructor() : Mapper<AppSettingsDto, AppSettings> {
    override fun map(input: AppSettingsDto): AppSettings = AppSettings(
        nonPremiumPlateFollowLimit = input.nonPremiumPlateFollowLimit ?: 0,
        nonPremiumPlateAlarmLimit = input.nonPremiumPlateAlarmLimit ?: 0,
        preApprovalMessageLimit = input.preApprovalMessageLimit ?: 0,
        commentReportThreshold = input.commentReportThreshold ?: 0,
        reportCommentMaxLength = input.reportCommentMaxLength ?: 250
    )
}
