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
    @SerializedName("totalFriendCounts") val totalFriendCounts: Int = 0,
    @SerializedName("averageGivenRating") val averageGivenRating: Double = 0.0,
    @SerializedName("reviewCount") val reviewCount: Int = 0,
    @SerializedName("joinedAt") val joinedAt: String? = null,
    @SerializedName("premiumActive") val premiumActive: Boolean = false,
    @SerializedName("premiumUntil") val premiumUntil: String? = null,
    @SerializedName("userSettings") val userSettings: UserSettingsDto,
    @SerializedName("reviewStatusCounts") val reviewStatusCounts: ReviewStatusTotals = ReviewStatusTotals(),
    @SerializedName("evaluationTotals") val evaluationTotals: ReviewStatusTotals? = null,
    @SerializedName("socialMediaLinks") val socialMediaLinks: List<SocialMediaLinkDto> = emptyList(),
    @SerializedName("plateReviews") val plateReviews: List<PlateReviewDto> = emptyList(),
    @SerializedName("friendRequests") val friendRequests: List<ProfileFriendRequestDto> = emptyList()
)
