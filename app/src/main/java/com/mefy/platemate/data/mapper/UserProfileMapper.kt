package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.dto.user.UserProfileDto
import com.mefy.platemate.domain.model.profile.UserProfile
import javax.inject.Inject

class UserProfileMapper @Inject constructor(
    private val socialMediaLinkMapper: SocialMediaLinkMapper,
    private val plateReviewPageMapper: PlateReviewPageMapper
) : Mapper<UserProfileDto, UserProfile> {
    override fun map(input: UserProfileDto): UserProfile = UserProfile(
        id = input.id,
        username = input.username,
        driverRating = input.driverRating,
        reviewCount = input.reviewCount,
        totalRatingSum = input.totalRatingSum,
        socialMediaLinks = socialMediaLinkMapper.mapList(input.socialMediaLinks),
        plateReviews = plateReviewPageMapper.map(input.plateReviews)
    )
}


