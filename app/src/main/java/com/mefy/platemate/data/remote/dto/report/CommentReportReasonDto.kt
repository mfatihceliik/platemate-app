package com.mefy.platemate.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class CommentReportReasonDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("requiresDescription") val requiresDescription: Boolean?
)
