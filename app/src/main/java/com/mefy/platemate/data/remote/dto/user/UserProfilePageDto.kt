package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.data.remote.dto.friend.FriendshipDto
import com.mefy.platemate.data.remote.dto.social.SocialPlatformDto

data class UserProfilePageDto(
    @SerializedName("profile") val profile: UserProfileDto,
    @SerializedName("pendingFriendRequests") val pendingFriendRequests: List<FriendshipDto> = emptyList(),
    @SerializedName("socialPlatforms") val socialPlatforms: List<SocialPlatformDto> = emptyList()
)
