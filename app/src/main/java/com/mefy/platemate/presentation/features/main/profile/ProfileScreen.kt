package com.mefy.platemate.presentation.features.main.profile

import com.mefy.platemate.presentation.common.state.ScreenStatus
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarAlignment
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMStatCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.profile.components.FriendRequestActivityCard
import com.mefy.platemate.presentation.features.main.profile.components.PlateReviewActivityCard
import com.mefy.platemate.presentation.features.main.profile.components.ProfileHeaderSection
import com.mefy.platemate.presentation.features.main.profile.components.ProfileShimmerContent
import com.mefy.platemate.presentation.features.main.profile.components.ProfileStatusSummarySection
import com.mefy.platemate.presentation.features.main.profile.model.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileSocialLinks
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.features.main.settings.components.SectionLabel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions

    val status = when {
        state.isInitialLoading -> ScreenStatus.Loading
        state.errorMessage != null -> ScreenStatus.Error(state.errorMessage)
        else -> ScreenStatus.Content
    }

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.main_tab_profile),
            alignment = PMTopBarAlignment.Start
        ),
        status = status,
        onRetry = { onAction(ProfileUiAction.RetryClicked) },
        loading = { innerPadding ->
            ProfileShimmerContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onAction(ProfileUiAction.RefreshRequested) },
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileContent(
                state = state,
                onAction = onAction,
                bottomInset = innerPadding.calculateBottomPadding(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    bottomInset: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyColumn(
        modifier = modifier
            .background(colors.background)
            .padding(dims.spacing.s12),
        contentPadding = PaddingValues(bottom = dims.spacing.s16 + bottomInset),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
    ) {
        item {
            ProfileHeaderSection(
                header = state.header,
                accountSummary = state.accountSummary
            )
        }

        if (state.stats.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
                ) {
                    state.stats.forEach { stat ->
                        PMStatCard(
                            value = stat.valueText,
                            label = stringResource(stat.labelResId),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            SectionLabel(
                text = stringResource(R.string.profile_section_status_summary)
            )
        }

        item {
            ProfileStatusSummarySection(statusSummary = state.statusSummary)
        }

        if (state.socialLinks.isNotEmpty()) {
            item {
                val uriHandler = LocalUriHandler.current
                PMCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16)
                ) {
                    UserProfileSocialLinks(
                        links = state.socialLinks.map {
                            UserProfileSocialLinkUiModel(platform = it.platform, url = it.url)
                        },
                        onLinkClick = { link ->
                            try {
                                uriHandler.openUri(link.url)
                            } catch (_: Exception) {
                            }
                        }
                    )
                }
            }
        }

        item {
            SectionLabel(
                text = stringResource(R.string.profile_section_recent_notifications)
            )
        }

        if (state.activities.isEmpty()) {
            item {
                PMText(
                    text = stringResource(R.string.profile_recent_empty),
                    fontSize = dims.fontSize.md,
                    color = colors.textTertiary,
                    modifier = Modifier.padding(horizontal = dims.spacing.s16)
                )
            }
        } else {
            items(
                items = state.activities,
                key = ProfileActivityUiModel::id,
                contentType = {
                    when (it) {
                        is PlateReviewNotificationItem -> "profile_review_activity"
                        is FriendRequestNotificationItem -> "profile_friend_activity"
                    }
                }
            ) { item ->
                when (item) {
                    is PlateReviewNotificationItem -> PlateReviewActivityCard(
                        item = item,
                        onClick = {
                            onAction(ProfileUiAction.PlateReviewClicked(item.normalizedPlateCode))
                        },
                        modifier = Modifier.testTag("profile_activity_${item.id}")
                    )

                    is FriendRequestNotificationItem -> FriendRequestActivityCard(
                        item = item,
                        onClick = { onAction(ProfileUiAction.FriendsClicked) },
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
            onAction = {}
        )
    }
}

@Preview(name = "Profile Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileScreen(
            state = previewState(isLoading = false),
            onAction = {}
        )
    }
}

@Preview(name = "Profile Shimmer", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ProfileShimmerPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileScreen(
            state = previewState(isLoading = true),
            onAction = {}
        )
    }
}

private fun previewState(isLoading: Boolean): ProfileUiState = ProfileUiState(
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
        ProfileStatUiModel("4.6", R.string.profile_stat_average_rating)
    ),
    statusSummary = ProfileStatusSummaryUiModel(
        approved = 124,
        pendingReview = 12,
        rejected = 6
    ),
    socialLinks = listOf(
        ProfileSocialLinkUiModel(id = 1, platform = "INSTAGRAM", url = "instagram.com/caner")
    ),
    activities = listOf(
        PlateReviewNotificationItem(
            id = "review_1",
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
            statusCode = "PENDING",
            createdAtText = "2026-05-26",
            sortKey = "2026-05-26T09:30:00Z"
        )
    )
)
