package com.mefy.platemate.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class AddChatMessageReportRequest(
    @SerializedName("reason") val reason: String
)
