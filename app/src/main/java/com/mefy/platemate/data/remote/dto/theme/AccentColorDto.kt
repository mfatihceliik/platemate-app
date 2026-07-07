package com.mefy.platemate.data.remote.dto.theme

import com.google.gson.annotations.SerializedName

data class AccentColorDto(
    @SerializedName("id") val id: Long,
    @SerializedName("hex") val hex: String?,
    @SerializedName("sortOrder") val sortOrder: Int?
)
