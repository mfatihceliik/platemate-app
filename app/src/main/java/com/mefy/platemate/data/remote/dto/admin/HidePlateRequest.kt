package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class HidePlateRequest(
    @SerializedName("reason") val reason: String
)
