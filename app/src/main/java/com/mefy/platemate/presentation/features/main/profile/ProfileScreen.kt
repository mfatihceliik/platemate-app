package com.mefy.platemate.presentation.features.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import com.mefy.platemate.presentation.features.main.profile.components.FriendRequestActivityCard
import com.mefy.platemate.presentation.features.main.profile.components.PlateReviewActivityCard
import com.mefy.platemate.presentation.features.main.profile.components.ProfileHeaderSection
import com.mefy.platemate.presentation.features.main.profile.components.ProfileStatusSummarySection
import com.mefy.platemate.presentation.features.main.profile.model.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileSocialLinks
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
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val uriHandler = LocalUriHandler.current

    // Stable, shared callbacks: activity cards skip recomposition while data is unchanged.
    val onReviewClick = remember(onAction) {
        { code: String -> onAction(ProfileUiAction.PlateReviewClicked(code)) }
    }
    val onFriendsClick = remember(onAction) {
        { onAction(ProfileUiAction.FriendsClicked) }
    }
    val onLinkClick = remember(uriHandler) {
        { link: ProfileSocialLinkUiModel ->
            try {
                uriHandler.openUri(link.url)
            } catch (_: Exception) {
            }
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .background(colors.background)
            .padding(dims.spacing.s12),
        contentPadding = PaddingValues(bottom = dims.spacing.s16 + innerPadding.calculateBottomPadding()),
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
            item(key = "social_links") {
                // Tıklama handler'ı stabil: her recomposition'da yeniden yaratılmaz.
                val onLinkClick = remember(uriHandler) {
                    { link: ProfileSocialLinkUiModel ->
                        try {
                            uriHandler.openUri(link.url)
                        } catch (_: Exception) {
                        }
                    }
                }
                // Bileşen kendi PMCard'ını çizer; burada ek sarmalayıcı yok.
                UserProfileSocialLinks(
                    links = state.socialLinks,
                    onLinkClick = onLinkClick
                )
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
                        onClick = onReviewClick,
                        modifier = Modifier.testTag("profile_activity_${item.id}")
                    )

                    is FriendRequestNotificationItem -> FriendRequestActivityCard(
                        item = item,
                        onClick = onFriendsClick,
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
