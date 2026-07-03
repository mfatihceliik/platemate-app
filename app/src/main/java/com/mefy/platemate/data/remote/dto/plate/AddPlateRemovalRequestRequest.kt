package com.mefy.platemate.data.remote.dto.plate

import com.google.gson.annotations.SerializedName

data class AddPlateRemovalRequestRequest(
    @SerializedName("reasonCode") val reasonCode: String,
    @SerializedName("description") val description: String
)
