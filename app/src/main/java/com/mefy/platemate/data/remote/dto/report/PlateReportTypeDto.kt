package com.mefy.platemate.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class PlateReportTypeDto(
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("iconKey") val iconKey: String?,
    @SerializedName("severityId") val severityId: Long?,
    @SerializedName("severityCode") val severityCode: String?,
    @SerializedName("colorHex") val colorHex: String?,
    @SerializedName("weight") val weight: Int?,
    @SerializedName("sortOrder") val sortOrder: Int?
)
