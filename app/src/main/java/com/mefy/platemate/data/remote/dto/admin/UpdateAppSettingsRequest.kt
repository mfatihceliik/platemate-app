package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class UpdateAppSettingsRequest(
    @SerializedName("nonPremiumPlateFollowLimit") val nonPremiumPlateFollowLimit: Int?,
    @SerializedName("nonPremiumPlateAlarmLimit") val nonPremiumPlateAlarmLimit: Int?,
    @SerializedName("preApprovalMessageLimit") val preApprovalMessageLimit: Int?,
    @SerializedName("commentReportThreshold") val commentReportThreshold: Int?,
    @SerializedName("reportCommentMaxLength") val reportCommentMaxLength: Int?
)
