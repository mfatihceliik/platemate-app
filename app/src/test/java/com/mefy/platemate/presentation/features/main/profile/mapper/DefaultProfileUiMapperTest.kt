package com.mefy.platemate.presentation.features.main.profile.mapper

import com.mefy.platemate.R
import com.mefy.platemate.core.common.pagination.ReviewStatusTotals
import com.mefy.platemate.domain.model.common.AppDateTime
import com.mefy.platemate.domain.model.profile.ProfileFriendRequest
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.review.Review
import com.mefy.platemate.domain.model.settings.UserSettings
import com.mefy.platemate.domain.usecase.search.FormatTurkishPlateInputUseCase
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import com.mefy.platemate.presentation.features.uimodel.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.uimodel.PlateReviewNotificationItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultProfileUiMapperTest {

    private val mapper = DefaultProfileUiMapper(
        formatTurkishPlateInputUseCase = FormatTurkishPlateInputUseCase(),
        validateTurkishPlateUseCase = ValidateTurkishPlateUseCase()
    )

    @Test
    fun mapProfile_mapsSummaryStatsAndMergedActivities() {
        val mapped = mapper.mapProfile(sampleProfile(), emptyList())

        assertEquals("caner", mapped.header.username)
        assertEquals("caner@platemate.com", mapped.accountSummary.email)
        assertEquals("2026-05-27", mapped.accountSummary.joinedAtText)
        assertEquals(2, mapped.stats.size)
        assertEquals(R.string.profile_stat_average_rating, mapped.stats[0].labelResId)
        assertEquals(R.string.profile_stat_friends, mapped.stats[1].labelResId)
        assertEquals(1, mapped.socialLinks.size)
        assertEquals(2, mapped.activities.size)
        assertTrue(mapped.activities.first() is PlateReviewNotificationItem)
        assertTrue(mapped.activities.last() is FriendRequestNotificationItem)
    }

    @Test
    fun mapProfile_normalizesBlankEmailForUiFallback() {
        val mapped = mapper.mapProfile(sampleProfile(email = "   "), emptyList())
        assertEquals("", mapped.accountSummary.email)
    }

    private fun sampleProfile(email: String? = "caner@platemate.com"): UserProfile = UserProfile(
        id = 7L,
        email = email,
        username = "caner",
        totalFriendCounts = 86,
        averageGivenRating = 4.65,
        reviewCount = 12,
        joinedAt = "2026-05-27T10:36:23.347Z",
        premiumActive = true,
        premiumUntil = "2026-06-30T00:00:00Z",
        userSettings = UserSettings(
            messagingEnabled = true,
            messageNotificationsEnabled = true,
            friendNotificationsEnabled = false
        ),
        reviewStatusCounts = ReviewStatusTotals(
            approved = 20,
            pendingReview = 5,
            rejected = 2,
            removedByUser = 1,
            removedByModerator = 1,
            removedByLegalRequest = 0
        ),
        evaluationTotals = null,
        socialMediaLinks = listOf(
            SocialMediaLink(
                id = 4L,
                platform = "INSTAGRAM",
                url = "https://instagram.com/caner"
            )
        ),
        plateReviews = listOf(
            Review(
                id = 101L,
                plateCode = "34ab1234",
                rating = 5,
                comment = "Tehlikeli",
                reviewStatus = "APPROVED",
                userId = 1L,
                reviewerUsername = "user1",
                createdAt = AppDateTime("2026-05-21T08:15:00Z"),
                updatedAt = null
            )
        ),
        friendRequests = listOf(
            ProfileFriendRequest(
                id = 201L,
                requesterUserId = 20L,
                requesterUsername = "fatih",
                addresseeUserId = 7L,
                addresseeUsername = "caner",
                statusCode = "PENDING",
                createdAt = AppDateTime("2026-05-20T08:15:00Z"),
                respondedAt = null,
                lastActionAt = null
            )
        )
    )
}
