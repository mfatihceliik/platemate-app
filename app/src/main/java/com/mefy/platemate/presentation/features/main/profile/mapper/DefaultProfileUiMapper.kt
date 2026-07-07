package com.mefy.platemate.presentation.features.main.profile.mapper

import com.mefy.platemate.R
import com.mefy.platemate.core.common.pagination.ReviewStatusTotals
import com.mefy.platemate.domain.model.profile.ProfileFriendRequest
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.social.SocialPlatform
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.common.text.NumberFormatter
import com.mefy.platemate.presentation.features.uimodel.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.uimodel.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackBg
import com.mefy.platemate.presentation.features.uimodel.SocialPlatformFallbackTint
import com.mefy.platemate.presentation.features.uimodel.toUiModel
import com.mefy.platemate.presentation.features.uimodel.SocialPlatform as SocialPlatformUiModel
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProfileUiMapper @Inject constructor(
    private val formatTurkishPlateInputUseCase: FormatTurkishPlateInputUseCase,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase
) : ProfileUiMapper {
    private companion object {
        const val UNKNOWN_STATUS = "UNKNOWN"
    }

    override fun mapProfile(profile: UserProfile, platforms: List<SocialPlatform>): ProfileUiData {
        val plateActivities = profile.plateReviews.map(::mapReviewActivity)
        val friendActivities = profile.friendRequests.map(::mapFriendRequestActivity)
        val mergedActivities = (plateActivities + friendActivities)
            .sortedByDescending(ProfileActivityUiModel::sortKey)
        val uiPlatforms = platforms.map { it.toUiModel() }

        return ProfileUiData(
            header = ProfileHeaderUiModel(username = profile.username),
            accountSummary = ProfileAccountSummaryUiModel(
                email = profile.email?.trim().orEmpty(),
                joinedAtText = formatIsoDate(profile.joinedAt),
                premiumUntilText = formatIsoDate(profile.premiumUntil),
                isPremiumActive = profile.premiumActive
            ),
            stats = listOf(
                ProfileStatUiModel(
                    valueText = formatRating(profile.averageGivenRating),
                    labelResId = R.string.profile_stat_average_rating
                ),
                ProfileStatUiModel(
                    valueText = profile.totalFriendCounts.toString(),
                    labelResId = R.string.profile_stat_friends
                )
            ),
            statusSummary = profile.reviewStatusCounts.toStatusSummaryUi(),
            socialLinks = profile.socialMediaLinks.map { link -> mapSocialLink(link, uiPlatforms) },
            activities = mergedActivities
        )
    }

    private fun mapReviewActivity(review: Review): PlateReviewNotificationItem {
        val normalizedPlate = validateTurkishPlateUseCase.normalize(review.plateCode)
        val formattedPlate = formatTurkishPlateInputUseCase(review.plateCode)
        val sortKey = review.createdAt?.iso8601.orEmpty()
        return PlateReviewNotificationItem(
            id = "review_${review.id}",
            normalizedPlateCode = normalizedPlate,
            plateCode = formattedPlate,
            ratingAverage = review.rating.toDouble(),
            commentCount = if (review.comment.isBlank()) 0L else 1L,
            reviewStatus = ProfileReviewStatusUi.from(review.reviewStatus),
            createdAtText = formatIsoDate(review.createdAt?.iso8601),
            sortKey = sortKey
        )
    }

    private fun mapFriendRequestActivity(request: ProfileFriendRequest): FriendRequestNotificationItem {
        val sortKey = request.lastActionAt?.iso8601
            ?: request.createdAt?.iso8601
            ?: request.respondedAt?.iso8601
            ?: ""
        return FriendRequestNotificationItem(
            id = "friend_${request.id}",
            friendUserId = request.requesterUserId,
            username = request.requesterUsername,
            statusCode = request.statusCode.ifBlank { UNKNOWN_STATUS },
            createdAtText = formatIsoDate(sortKey),
            sortKey = sortKey
        )
    }

    private fun mapSocialLink(link: SocialMediaLink, platforms: List<SocialPlatformUiModel>): ProfileSocialLinkUiModel {
        // Bilinmeyen platform jenerik link görünümüne düşer; !! ile çökmek yerine fallback.
        val platform = platforms.find { it.code.equals(link.platform, ignoreCase = true) }

        return ProfileSocialLinkUiModel(
            id = link.id,
            platform = link.platform,
            url = link.url,
            iconUrl = platform?.iconUrl,
            backgroundColor = platform?.backgroundColor ?: SocialPlatformFallbackBg,
            iconTint = platform?.iconTint ?: SocialPlatformFallbackTint
        )
    }

    private fun ReviewStatusTotals.toStatusSummaryUi(): ProfileStatusSummaryUiModel =
        ProfileStatusSummaryUiModel(
            approved = approved,
            pendingReview = pendingReview,
            rejected = rejected,
            removedByUser = removedByUser,
            removedByModerator = removedByModerator,
            removedByLegalRequest = removedByLegalRequest
        )

    private fun formatRating(value: Double): String = NumberFormatter.formatRating(value)

    private fun formatIsoDate(rawIsoDate: String?): String {
        val value = rawIsoDate?.trim().orEmpty()
        if (value.isBlank()) return "-"
        return value.take(10)
    }


}
