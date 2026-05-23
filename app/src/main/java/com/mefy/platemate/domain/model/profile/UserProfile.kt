package com.mefy.platemate.domain.model.profile

import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.domain.model.review.Review

data class UserProfile(
    val id: Long,
    val username: String,
    val driverRating: Double,
    val reviewCount: Int,
    val totalRatingSum: Int,
    val socialMediaLinks: List<SocialMediaLink>,
    val plateReviews: PagedResult<Review>
)
