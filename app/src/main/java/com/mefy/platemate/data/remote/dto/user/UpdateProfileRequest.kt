package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("profilePhotoUrl") val profilePhotoUrl: String?
)
