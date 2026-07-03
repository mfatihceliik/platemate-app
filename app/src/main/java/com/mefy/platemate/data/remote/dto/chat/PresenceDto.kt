package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class PresenceDto(
    @SerializedName("userId") val userId: Long? = null,
    @SerializedName("online") val online: Boolean = false
)
