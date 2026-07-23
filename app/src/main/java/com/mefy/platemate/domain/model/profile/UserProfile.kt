package com.mefy.platemate.domain.model.profile

import com.mefy.platemate.core.common.pagination.ReviewStatusTotals
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.settings.UserSettings

data class UserProfile(
    val id: Long,
    val email: String?,
    val username: String,
    val displayName: String? = null,
    val bio: String? = null,
    val profilePhotoUrl: String? = null,
    val verified: Boolean = false,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val friendshipStatus: ProfileFriendshipStatus = ProfileFriendshipStatus.NONE,
    val friendshipId: Long? = null,
    val totalFriendCounts: Int,
    val averageGivenRating: Double,
    val reviewCount: Int,
    val joinedAt: String?,
    val premiumActive: Boolean,
    val isAdmin: Boolean = false,
    val premiumUntil: String?,
    val userSettings: UserSettings?,
    val reviewStatusCounts: ReviewStatusTotals,
    val evaluationTotals: ReviewStatusTotals?,
    val socialMediaLinks: List<SocialMediaLink>,
    val plateReviews: List<Review>,
    val friendRequests: List<ProfileFriendRequest>
)
