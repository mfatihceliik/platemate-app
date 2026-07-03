package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class PlateRemovalRequestDto(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("requesterUsername") val requesterUsername: String?,
    @SerializedName("requesterEmail") val requesterEmail: String?,
    @SerializedName("reasonCode") val reasonCode: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("createdAt") val createdAt: String?
)
