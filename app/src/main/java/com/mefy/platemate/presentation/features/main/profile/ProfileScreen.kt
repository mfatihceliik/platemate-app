package com.mefy.platemate.presentation.features.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMStatCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTabRow
import com.mefy.platemate.presentation.components.PMTabItem
import com.mefy.platemate.presentation.features.main.profile.components.FriendRequestActivityCard
import com.mefy.platemate.presentation.features.main.profile.components.PlateReviewActivityCard
import com.mefy.platemate.presentation.features.main.profile.components.ProfileHeaderSection
import com.mefy.platemate.presentation.features.main.profile.components.ProfileStatusSummarySection
import com.mefy.platemate.presentation.features.uimodel.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.uimodel.FriendRequestStatusUi
import com.mefy.platemate.presentation.features.uimodel.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileSocialLinks
import com.mefy.platemate.presentation.features.uimodel.ProfileStatUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.components.PMSectionLabel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues()
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val fontSize = PMTheme.fontSize
    val uriHandler = LocalUriHandler.current

    val onReviewClick = remember(onAction) {
        { code: String, reviewId: Long -> onAction(ProfileUiAction.PlateReviewClicked(code, reviewId)) }
    }
    val onFriendsClick = remember(onAction) {
        { onAction(ProfileUiAction.FriendsClicked) }
    }
    val onFriendRequestClick = remember(onAction) {
        { onAction(ProfileUiAction.FriendRequestActivityClicked) }
    }
    val onStatusClick = remember(onAction) {
        { status: String -> onAction(ProfileUiAction.StatusSummaryClicked(status)) }
    }
    val plateActivities = remember(state.activities) {
        state.activities.filterIsInstance<PlateReviewNotificationItem>()
    }
    val friendActivities = remember(state.activities) {
        state.activities.filterIsInstance<FriendRequestNotificationItem>()
    }
    val onLinkClick = remember(uriHandler) {
        { link: ProfileSocialLinkUiModel ->
            try {
                uriHandler.openUri(link.url)
            } catch (_: Exception) {}
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize(),
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        item {
            ProfileHeaderSection(
                header = state.header,
                accountSummary = state.accountSummary,
                onFriendsClick = onFriendsClick,
                pendingFriendRequestCount = state.pendingFriendRequestCount
            )
        }

        if (state.stats.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.s8)
                ) {
                    state.stats.forEach { stat ->
                        PMStatCard(
                            value = stat.valueText,
                            label = stringResource(stat.labelResId),
                            onClick = if (stat.labelResId == R.string.profile_stat_friends) onFriendsClick else null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            PMSectionLabel(
                text = stringResource(R.string.profile_section_status_summary)
            )
        }

        item {
            ProfileStatusSummarySection(
                statusSummary = state.statusSummary,
                onStatusClick = onStatusClick
            )
        }

        if (state.socialLinks.isNotEmpty()) {
            item(key = "social_links") {
                UserProfileSocialLinks(
                    links = state.socialLinks,
                    onLinkClick = onLinkClick
                )
            }
        }

        item {
            PMSectionLabel(
                text = stringResource(R.string.profile_section_recent_notifications)
            )
        }

        item {
            PMTabRow(
                selectedTabIndex = state.selectedActivityTab,
                tabs = listOf(
                    PMTabItem(
                        title = stringResource(R.string.profile_activity_tab_plate),
                        icon = Icons.Filled.DirectionsCar
                    ),
                    PMTabItem(
                        title = stringResource(R.string.profile_activity_tab_friends),
                        icon = Icons.Filled.Person
                    )
                ),
                onTabSelected = { onAction(ProfileUiAction.ActivityTabChanged(it)) }
            )
        }

        if (state.selectedActivityTab == 0) {
            if (plateActivities.isEmpty()) {
                item {
                    PMText(
                        text = stringResource(R.string.profile_recent_empty_plate),
                        fontSize = fontSize.md,
                        color = colors.textPrimary
                    )
                }
            } else {
                items(
                    items = plateActivities,
                    key = ProfileActivityUiModel::id,
                    contentType = { "profile_review_activity" }
                ) { item ->
                    PlateReviewActivityCard(
                        item = item,
                        onClick = onReviewClick,
                        modifier = Modifier.testTag("profile_activity_${item.id}")
                    )
                }
            }
        } else {
            if (friendActivities.isEmpty()) {
                item {
                    PMText(
                        text = stringResource(R.string.profile_recent_empty_friends),
                        fontSize = fontSize.md,
                        color = colors.textPrimary
                    )
                }
            } else {
                items(
                    items = friendActivities,
                    key = ProfileActivityUiModel::id,
                    contentType = { "profile_friend_activity" }
                ) { item ->
                    FriendRequestActivityCard(
                        item = item,
                        onClick = onFriendRequestClick,
                        modifier = Modifier.testTag("profile_activity_${item.id}")
                    )
                }
            }
        }
    }
}

@Preview(name = "Profile Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ProfileLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileScreen(
            state = previewState(isLoading = false),
            onAction = {},
        )
    }
}

@Preview(name = "Profile Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileScreen(
            state = previewState(isLoading = false),
            onAction = {},
        )
    }
}

private fun previewState(isLoading: Boolean = false): ProfileUiState = ProfileUiState(
    isInitialLoading = isLoading,
    header = ProfileHeaderUiModel(username = "Caner Yıldırım"),
    accountSummary = ProfileAccountSummaryUiModel(
        email = "caner@platemate.com",
        joinedAtText = "Ocak 2025",
        premiumUntilText = "2026-06-30",
        isPremiumActive = true
    ),
    stats = listOf(
        ProfileStatUiModel("42", R.string.profile_stat_average_rating),
        ProfileStatUiModel("318", R.string.profile_stat_friends),
    ),
    statusSummary = ProfileStatusSummaryUiModel(
        approved = 124,
        pendingReview = 12,
        rejected = 6
    ),
    socialLinks = listOf(
        ProfileSocialLinkUiModel(
            id = 1,
            platform = "INSTAGRAM",
            url = "instagram.com/caner",
            iconUrl = null,
            backgroundColor = Color(0xFFFDF2F8),
            iconTint = Color(0xFFDB2777)
        )
    ),
    activities = listOf(
        PlateReviewNotificationItem(
            id = "review_1",
            reviewId = 1L,
            normalizedPlateCode = "34AB1234",
            plateCode = "34 AB 1234",
            ratingAverage = 4.0,
            commentCount = 1,
            reviewStatus = ProfileReviewStatusUi.APPROVED,
            createdAtText = "2026-05-27",
            sortKey = "2026-05-27T10:00:00Z"
        ),
        FriendRequestNotificationItem(
            id = "friend_1",
            friendUserId = 7,
            username = "fatih",
            status = FriendRequestStatusUi.REQUESTED,
            createdAtText = "2026-05-26",
            sortKey = "2026-05-26T09:30:00Z"
        )
    )
)
