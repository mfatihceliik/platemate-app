package com.mefy.platemate.presentation.features.main.profile.reducer

import androidx.compose.ui.graphics.Color
import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.main.profile.ProfileUiState
import com.mefy.platemate.presentation.features.main.profile.mapper.ProfileUiData
import com.mefy.platemate.presentation.features.uimodel.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.common.text.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileStateReducerTest {

    private val reducer = ProfileStateReducer()

    @Test
    fun onInitialLoading_setsLoadingTrue() {
        val state = reducer.onInitialLoading(ProfileUiState(isInitialLoading = false))
        assertTrue(state.isInitialLoading)
    }

    @Test
    fun onProfileLoaded_populatesContentAndTurnsOffLoading() {
        val mapped = ProfileUiData(
            header = ProfileHeaderUiModel(username = "fatih"),
            accountSummary = ProfileAccountSummaryUiModel(
                email = "fatih@platemate.com",
                joinedAtText = "2026-05-27",
                premiumUntilText = "2026-06-30",
                isPremiumActive = true
            ),
            stats = listOf(
                ProfileStatUiModel(valueText = "4.6", labelResId = R.string.profile_stat_average_rating),
                ProfileStatUiModel(valueText = "12", labelResId = R.string.profile_stat_friends)
            ),
            statusSummary = ProfileStatusSummaryUiModel(approved = 10, pendingReview = 2, rejected = 1),
            socialLinks = listOf(
                ProfileSocialLinkUiModel(
                    id = 1,
                    platform = "INSTAGRAM",
                    url = "x",
                    iconUrl = null,
                    backgroundColor = Color(0xFFFDF2F8),
                    iconTint = Color(0xFFDB2777)
                )
            ),
            activities = listOf(
                FriendRequestNotificationItem(
                    id = "friend_1",
                    friendUserId = 9L,
                    username = "user",
                    statusCode = "PENDING",
                    createdAtText = "2026-05-20",
                    sortKey = "2026-05-20T10:00:00Z"
                )
            )
        )

        val state = reducer.onProfileLoaded(ProfileUiState(), mapped)

        assertFalse(state.isInitialLoading)
        assertEquals("fatih", state.header.username)
        assertEquals(2, state.stats.size)
        assertEquals(1, state.socialLinks.size)
        assertEquals(1, state.activities.size)
    }

    @Test
    fun onLoadFailed_turnsOffLoadingAndSetsError() {
        val state = reducer.onLoadFailed(ProfileUiState(isInitialLoading = true), UiText.Dynamic("err"))
        assertEquals(UiText.Dynamic("err"), state.errorMessage)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun onRefreshing_setsRefreshingTrue() {
        val state = reducer.onRefreshing(ProfileUiState())
        assertTrue(state.isRefreshing)
    }

    @Test
    fun onProfileLoaded_clearsRefreshingAndError() {
        val mapped = ProfileUiData(
            header = ProfileHeaderUiModel(username = "fatih"),
            accountSummary = ProfileAccountSummaryUiModel(),
            stats = emptyList(),
            statusSummary = ProfileStatusSummaryUiModel(),
            socialLinks = emptyList(),
            activities = emptyList()
        )

        val state = reducer.onProfileLoaded(
            ProfileUiState(isRefreshing = true, errorMessage = UiText.Dynamic("x")),
            mapped
        )

        assertFalse(state.isRefreshing)
        assertNull(state.errorMessage)
    }

    @Test
    fun onRefreshFailed_stopsRefreshingWithoutError() {
        val state = reducer.onRefreshFailed(ProfileUiState(isRefreshing = true))
        assertFalse(state.isRefreshing)
    }
}
