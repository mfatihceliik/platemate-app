package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName
import com.mefy.platemate.core.common.pagination.PagedResult
import com.mefy.platemate.data.remote.dto.plate.PlateReviewDto
import com.mefy.platemate.data.remote.dto.social.SocialMediaLinkDto

data class UserProfileDto(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("driverRating") val driverRating: Double,
    @SerializedName("reviewCount") val reviewCount: Int,
    @SerializedName("totalRatingSum") val totalRatingSum: Int,
    @SerializedName("socialMediaLinks") val socialMediaLinks: List<SocialMediaLinkDto>,
    @SerializedName("plateReviews") val plateReviews: PagedResult<PlateReviewDto>
)
