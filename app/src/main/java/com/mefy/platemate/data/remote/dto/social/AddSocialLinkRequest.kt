package com.mefy.platemate.data.remote.dto.social

import com.google.gson.annotations.SerializedName

data class AddSocialLinkRequest(
    @SerializedName("platform") val platform: SocialPlatformDto,
    @SerializedName("url") val url: String
)