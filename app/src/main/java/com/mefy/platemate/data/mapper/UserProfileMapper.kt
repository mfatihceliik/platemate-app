package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.dto.user.UserProfileDto
import com.mefy.platemate.domain.model.profile.ProfileFriendshipStatus
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.profile.fromWire
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
        displayName = input.displayName,
        bio = input.bio,
        profilePhotoUrl = input.profilePhotoUrl,
        verified = input.verified,
        followerCount = input.followerCount,
        followingCount = input.followingCount,
        isFollowing = input.isFollowing,
        friendshipStatus = ProfileFriendshipStatus.fromWire(input.friendshipStatus),
        friendshipId = input.friendshipId,
        totalFriendCounts = input.totalFriendCounts,
        averageGivenRating = input.averageGivenRating,
        reviewCount = input.reviewCount,
        joinedAt = input.joinedAt,
        premiumActive = input.premiumActive,
        isAdmin = input.isAdmin,
        premiumUntil = input.premiumUntil,
        userSettings = input.userSettings?.let(userSettingsMapper::map),
        reviewStatusCounts = input.reviewStatusCounts,
        evaluationTotals = input.evaluationTotals,
        socialMediaLinks = socialMediaLinkMapper.mapList(input.socialMediaLinks),
        plateReviews = plateReviewMapper.mapList(input.plateReviews),
        friendRequests = profileFriendRequestMapper.mapList(input.friendRequests)
    )
}
