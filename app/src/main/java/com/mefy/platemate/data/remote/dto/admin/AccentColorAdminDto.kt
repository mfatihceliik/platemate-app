package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class AccentColorAdminDto(
    @SerializedName("id") val id: Long,
    @SerializedName("hex") val hex: String?,
    @SerializedName("sortOrder") val sortOrder: Int?,
    @SerializedName("active") val active: Boolean
)
