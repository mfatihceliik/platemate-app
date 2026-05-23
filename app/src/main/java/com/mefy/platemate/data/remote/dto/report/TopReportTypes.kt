package com.mefy.platemate.data.remote.dto.report

import com.google.gson.annotations.SerializedName

data class TopReportTypes(
    @SerializedName("code") val code: String,
    @SerializedName("label") val label: String,
    @SerializedName("description") val description: String,
    @SerializedName("iconKey") val iconKey: String,
    @SerializedName("severity") val severity: String,
    @SerializedName("colorHex") val colorHex: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sortOrder") val sortOrder: Int,
)


