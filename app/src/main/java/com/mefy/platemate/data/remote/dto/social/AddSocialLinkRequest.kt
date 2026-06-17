package com.mefy.platemate.data.remote.dto.social

import com.google.gson.annotations.SerializedName

data class AddSocialLinkRequest(
    @SerializedName("platformCode") val platformCode: String,
    @SerializedName("url") val url: String
)