package com.mefy.platemate.domain.model.admin

data class AppSettings(
    val nonPremiumPlateFollowLimit: Int,
    val nonPremiumPlateAlarmLimit: Int,
    val preApprovalMessageLimit: Int,
    val commentReportThreshold: Int
)
