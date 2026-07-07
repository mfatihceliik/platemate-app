package com.mefy.platemate.data.remote.dto.admin

import com.google.gson.annotations.SerializedName

data class AdminCommentModerationRequest(
    @SerializedName("reason") val reason: String?
)
