package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DiscoveryReportTypeCountDto(
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("colorHex") val colorHex: String?,
    @SerializedName("iconKey") val iconKey: String?,
    @SerializedName("count") val count: Long?
)
