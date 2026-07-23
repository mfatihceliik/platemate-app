package com.mefy.platemate.data.remote.dto.follow

import com.google.gson.annotations.SerializedName

data class FollowListItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("profilePhotoUrl") val profilePhotoUrl: String?,
    @SerializedName("isFollowing") val isFollowing: Boolean?
)
