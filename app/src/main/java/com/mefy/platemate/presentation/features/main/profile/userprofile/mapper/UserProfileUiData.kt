package com.mefy.platemate.presentation.features.main.profile.userprofile.mapper

import com.mefy.platemate.domain.model.profile.ProfileFriendshipStatus
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel

data class UserProfileUiData(
    val displayName: String,
    val username: String,
    val bio: String,
    val isVerified: Boolean,
    val isOnline: Boolean,
    val friendshipStatus: ProfileFriendshipStatus,
    val friendshipId: Long?,
    val reviewCount: Int,
    val followerCount: String,
    val followingCount: Int,
    val socialLinks: List<ProfileSocialLinkUiModel>,
    val approvedReviews: List<UserProfileReviewUiModel>
)
