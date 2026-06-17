package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.dto.user.UserProfileDto
import com.mefy.platemate.domain.model.profile.UserProfile
import javax.inject.Inject

class UserProfileMapper @Inject constructor(
    private val userSettingsMapper: UserSettingsMapper,
    private val socialMediaLinkMapper: SocialMediaLinkMapper,
    private val plateReviewMapper: PlateReviewMapper,
    private val profileFriendRequestMapper: ProfileFriendRequestMapper
) : Mapper<UserProfileDto, UserProfile> {
    override fun map(input: UserProfileDto): UserProfile = UserProfile(
        id = input.id,
        email = input.email,
        username = input.username,
        totalFriendCounts = input.totalFriendCounts,
        averageGivenRating = input.averageGivenRating,
        reviewCount = input.reviewCount,
        joinedAt = input.joinedAt,
        premiumActive = input.premiumActive,
        premiumUntil = input.premiumUntil,
        userSettings = userSettingsMapper.map(input.userSettings),
        reviewStatusCounts = input.reviewStatusCounts,
        evaluationTotals = input.evaluationTotals,
        socialMediaLinks = socialMediaLinkMapper.mapList(input.socialMediaLinks),
        plateReviews = plateReviewMapper.mapList(input.plateReviews),
        friendRequests = profileFriendRequestMapper.mapList(input.friendRequests)
    )
}
