package com.mefy.platemate.presentation.features.main.profile

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.uimodel.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.uimodel.FriendRequestStatusUi
import com.mefy.platemate.presentation.features.uimodel.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.uimodel.ProfileStatUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shimmerVisible_whenInitialLoading() {
        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                ProfileScreen(state = sampleState(isInitialLoading = true), onAction = {},)
            }
        }

        composeRule.onNodeWithTag("profile_shimmer_root").assertIsDisplayed()
    }

    @Test
    fun contentState_displaysHeaderAndActivityCards() {
        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                ProfileScreen(state = sampleState(isInitialLoading = false), onAction = {},)
            }
        }

        composeRule.onNodeWithTag("profile_content_root").assertIsDisplayed()
        composeRule.onNodeWithTag("profile_settings_action").assertIsDisplayed()
        composeRule.onNodeWithTag("profile_friends_stat").assertIsDisplayed()
        composeRule.onNodeWithTag("profile_activity_review_1").assertIsDisplayed()
        composeRule.onNodeWithTag("profile_activity_friend_2").assertIsDisplayed()
    }

    @Test
    fun friendActivityClick_dispatchesFriendsAction() {
        var lastAction: ProfileUiAction? = null

        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                ProfileScreen(
                    state = sampleState(isInitialLoading = false),
                    onAction = { lastAction = it },
                )
            }
        }

        composeRule.onNodeWithTag("profile_activity_friend_2").performClick()
        assertEquals(ProfileUiAction.FriendsClicked, lastAction)
    }

    @Test
    fun emailFallback_visibleWhenBlank() {
        composeRule.setContent {
            PlateMateTheme(darkTheme = true, dynamicColor = false) {
                ProfileScreen(
                    state = sampleState(isInitialLoading = false).copy(
                        accountSummary = ProfileAccountSummaryUiModel(
                            email = "   ",
                            joinedAtText = "2024-02-11",
                            premiumUntilText = "2026-06-30",
                            isPremiumActive = true
                        )
                    ),
                    onAction = {},
                )
            }
        }

        composeRule.onNodeWithText(getString(R.string.profile_email_not_specified)).assertIsDisplayed()
    }

    private fun sampleState(isInitialLoading: Boolean): ProfileUiState = ProfileUiState(
        isInitialLoading = isInitialLoading,
        header = ProfileHeaderUiModel(username = "Caner Yildirim"),
        accountSummary = ProfileAccountSummaryUiModel(
            email = "caner@platemate.com",
            joinedAtText = "2024-02-11",
            premiumUntilText = "2026-06-30",
            isPremiumActive = true
        ),
        stats = listOf(
            ProfileStatUiModel(valueText = "4.9", labelResId = R.string.profile_stat_average_rating),
            ProfileStatUiModel(valueText = "86", labelResId = R.string.profile_stat_friends)
        ),
        statusSummary = ProfileStatusSummaryUiModel(
            approved = 88,
            pendingReview = 21,
            rejected = 15
        ),
        activities = listOf(
            PlateReviewNotificationItem(
                id = "review_1",
                reviewId = 1L,
                normalizedPlateCode = "34AB1234",
                plateCode = "34 AB 1234",
                ratingAverage = 4.5,
                commentCount = 1L,
                reviewStatus = ProfileReviewStatusUi.APPROVED,
                createdAtText = "2026-05-27",
                sortKey = "2026-05-27T10:00:00Z"
            ),
            FriendRequestNotificationItem(
                id = "friend_2",
                friendUserId = 7L,
                username = "fatih",
                status = FriendRequestStatusUi.REQUESTED,
                createdAtText = "2026-05-26",
                sortKey = "2026-05-26T10:00:00Z"
            )
        )
    )

    private fun getString(resId: Int): String =
        ApplicationProvider.getApplicationContext<Context>().getString(resId)
}
