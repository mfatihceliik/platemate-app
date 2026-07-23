package com.mefy.platemate.domain.model.profile

import com.mefy.platemate.domain.model.social.Friendship
import com.mefy.platemate.domain.model.social.SocialPlatform

data class ProfilePage(
    val profile: UserProfile,
    val pendingFriendRequests: List<Friendship>,
    val socialPlatforms: List<SocialPlatform>
)
