package com.mefy.platemate.data.remote.dto.subscription

import com.google.gson.annotations.SerializedName

data class ActivateSubscriptionRequest(
    @SerializedName("days") val days: Int
)