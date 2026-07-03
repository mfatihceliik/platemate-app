package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class PlateReportTypeAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("iconKey") val iconKey: String?,
    @SerializedName("severityCode") val severityCode: String?,
    @SerializedName("colorHex") val colorHex: String?,
    @SerializedName("weight") val weight: Int?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("active") val active: Boolean
)
