package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class CommentReportReasonAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("requiresDescription") val requiresDescription: Boolean,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("active") val active: Boolean
)
