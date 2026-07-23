package com.mefy.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DiscoveryTabOptionDto(
    @SerializedName("code") val code: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("sortOrder") val sortOrder: Int?
)
