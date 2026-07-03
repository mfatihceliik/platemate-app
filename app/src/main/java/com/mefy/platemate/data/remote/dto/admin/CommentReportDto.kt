package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class CommentReportDto(
    @SerializedName("id") val id: Long,
    @SerializedName("commentId") val commentId: Long?,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("reasonCode") val reasonCode: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("createdAt") val createdAt: String?
)
