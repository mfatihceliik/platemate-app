package com.mefy.platemate.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class AddCommentReportRequest(
    @SerializedName("reasonCode") val reasonCode: String,
    @SerializedName("description") val description: String?
)
