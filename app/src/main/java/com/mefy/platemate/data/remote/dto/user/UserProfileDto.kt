package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.core.common.pagination.ReviewStatusTotals
import com.mefy.platemate.data.remote.dto.friend.ProfileFriendRequestDto
import com.mefy.platemate.data.remote.dto.plate.PlateReviewDto
import com.mefy.platemate.data.remote.dto.social.SocialMediaLinkDto

data class UserProfileDto(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String? = null,
    @SerializedName("username") val username: String,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("profilePhotoUrl") val profilePhotoUrl: String? = null,
    @SerializedName("verified") val verified: Boolean = false,
    @SerializedName("followerCount") val followerCount: Int = 0,
    @SerializedName("followingCount") val followingCount: Int = 0,
    @SerializedName("isFollowing") val isFollowing: Boolean = false,
    @SerializedName("friendshipStatus") val friendshipStatus: String = "NONE",
    @SerializedName("friendshipId") val friendshipId: Long? = null,
    @SerializedName("totalFriendCounts") val totalFriendCounts: Int = 0,
    @SerializedName("averageGivenRating") val averageGivenRating: Double = 0.0,
    @SerializedName("reviewCount") val reviewCount: Int = 0,
    @SerializedName("joinedAt") val joinedAt: String? = null,
    @SerializedName("premiumActive") val premiumActive: Boolean = false,
    @SerializedName("admin") val isAdmin: Boolean = false,
    @SerializedName("premiumUntil") val premiumUntil: String? = null,
    @SerializedName("userSettings") val userSettings: UserSettingsDto? = null,
    @SerializedName("reviewStatusCounts") val reviewStatusCounts: ReviewStatusTotals = ReviewStatusTotals(),
    @SerializedName("evaluationTotals") val evaluationTotals: ReviewStatusTotals? = null,
    @SerializedName("socialMediaLinks") val socialMediaLinks: List<SocialMediaLinkDto> = emptyList(),
    @SerializedName("plateReviews") val plateReviews: List<PlateReviewDto> = emptyList(),
    @SerializedName("friendRequests") val friendRequests: List<ProfileFriendRequestDto> = emptyList()
)
