package com.mefy.platemate.data.remote.dto.settings

import com.google.gson.annotations.SerializedName

data class UpdateAppearanceRequest(
    @SerializedName("themeMode") val themeMode: String,
    @SerializedName("accentHex") val accentHex: String?
)
