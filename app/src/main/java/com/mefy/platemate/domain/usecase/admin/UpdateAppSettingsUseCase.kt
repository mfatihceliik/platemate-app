package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class UpdateAppSettingsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(
        nonPremiumPlateFollowLimit: Int,
        nonPremiumPlateAlarmLimit: Int,
        preApprovalMessageLimit: Int,
        commentReportThreshold: Int,
        reportCommentMaxLength: Int
    ) = repository.updateAppSettings(
        nonPremiumPlateFollowLimit,
        nonPremiumPlateAlarmLimit,
        preApprovalMessageLimit,
        commentReportThreshold,
        reportCommentMaxLength
    )
}
