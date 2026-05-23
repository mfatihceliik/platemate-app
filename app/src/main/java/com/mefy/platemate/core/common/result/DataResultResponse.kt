package com.mefy.platemate.core.common.result

import com.google.gson.annotations.SerializedName

data class DataResultResponse<T>(
    @SerializedName("message") val message: String?,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: T?
)