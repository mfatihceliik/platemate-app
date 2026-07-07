package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class PlateAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("plateCode") val plateCode: String?,
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("hiddenReason") val hiddenReason: String?,
    @SerializedName("reviewCount") val reviewCount: Int?,
    @SerializedName("reportCount") val reportCount: Int?,
    @SerializedName("updatedAt") val updatedAt: String?
)
