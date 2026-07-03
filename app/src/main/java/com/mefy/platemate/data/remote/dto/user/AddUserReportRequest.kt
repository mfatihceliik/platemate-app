package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class AddUserReportRequest(
    @SerializedName("reason") val reason: String
)
