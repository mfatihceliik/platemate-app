package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserSearchResultDto(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("profilePhotoUrl") val profilePhotoUrl: String?
)
