package com.mefy.platemate.data.remote.dto.fcm

import com.google.gson.annotations.SerializedName

data class RegisterFcmTokenRequest(
    @SerializedName("token") val token: String,
    @SerializedName("deviceId") val deviceId: String
)