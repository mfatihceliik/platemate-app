package com.mefy.platemate.presentation.features.main.profile.userprofile.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.presentation.common.formatter.NumberFormatter
import com.mefy.platemate.presentation.common.text.CityNameResolver
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.SocialPlatform
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackBg
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackTint
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel
import javax.inject.Inject

data class UserProfileMappingInput(
    val profile: UserProfile,
    val platforms: List<SocialPlatform>
)

class UserProfileUiMapper @Inject constructor() : Mapper<UserProfileMappingInput, UserProfileUiData> {

    override fun map(input: UserProfileMappingInput): UserProfileUiData {
        val profile = input.profile
        val platforms = input.platforms
        val display = profile.displayName?.takeIf { it.isNotBlank() } ?: profile.username
        return UserProfileUiData(
            displayName = display,
            username = "@${profile.username}",
            bio = profile.bio.orEmpty(),
            isVerified = profile.premiumActive || profile.isAdmin,
            isOnline = false,
            friendshipStatus = profile.friendshipStatus,
            friendshipId = profile.friendshipId,
            reviewCount = profile.reviewCount,
            followerCount = NumberFormatter.formatCompact(profile.followerCount.toLong()),
            followingCount = profile.followingCount,
            socialLinks = profile.socialMediaLinks.map { link ->
                val platform = platforms.find { it.code.equals(link.platform, ignoreCase = true) }
                ProfileSocialLinkUiModel(
                    id = link.id,
                    platform = link.platform,
                    url = link.url,
                    iconUrl = platform?.iconUrl,
                    backgroundColor = platform?.backgroundColor ?: SocialPlatformFallbackBg,
                    iconTint = platform?.iconTint ?: SocialPlatformFallbackTint
                )
            },
            approvedReviews = profile.plateReviews.map(::mapReview)
        )
    }

    private fun mapReview(review: Review): UserProfileReviewUiModel = UserProfileReviewUiModel(
        id = review.id,
        plateCode = review.plateCode.take(2),
        plateNumber = review.plateCode,
        city = CityNameResolver.resolveCityName(cityName = null, plateCode = review.plateCode).orEmpty(),
        date = review.createdAt?.iso8601?.substringBefore("T").orEmpty(),
        rating = review.rating.toFloat(),
        tags = emptyList(),
        comment = review.comment
    )
}
