package com.mefy.platemate.core.common.result

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("success") val success: Boolean
)