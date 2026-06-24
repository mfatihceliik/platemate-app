package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class PlateTagSummaryItemDto(
    @SerializedName("code") val code: String,
    @SerializedName("label") val label: String,
    @SerializedName("iconKey") val iconKey: String,
    @SerializedName("colorHex") val colorHex: String,
    @SerializedName("count") val count: Long
)
