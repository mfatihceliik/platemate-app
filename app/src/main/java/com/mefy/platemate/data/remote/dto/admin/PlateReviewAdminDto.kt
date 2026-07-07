package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class PlateReviewAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("userId") val userId: Long?,
    @SerializedName("username") val username: String?,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("moderationReason") val moderationReason: String?,
    @SerializedName("reportCount") val reportCount: Int?,
    @SerializedName("reportTags") val reportTags: List<String>?,
    @SerializedName("createdAt") val createdAt: String?
)
