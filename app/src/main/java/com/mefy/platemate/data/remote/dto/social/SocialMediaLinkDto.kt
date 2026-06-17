package com.mefy.platemate.data.remote.dto.social

import com.google.gson.annotations.SerializedName

data class SocialMediaLinkDto(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("platformCode") val platformCode: String? = null,
    @SerializedName("platformId") val platformId: Long? = null,
    @SerializedName("url") val url: String
)

