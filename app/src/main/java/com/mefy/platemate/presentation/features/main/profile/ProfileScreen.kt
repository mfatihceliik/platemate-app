package com.mefy.platemate.presentation.features.main.profile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMStatCard
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PlateCard
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.model.PlateCardDensity
import com.mefy.platemate.presentation.features.main.profile.model.FriendRequestNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.PlateReviewNotificationItem
import com.mefy.platemate.presentation.features.main.profile.model.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    PMBaseScreen(modifier = modifier) { innerPadding ->
        Crossfade(
            targetState = state.isInitialLoading,
            label = "profile_loading_crossfade",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { isInitialLoading ->
            if (isInitialLoading) {
                ProfileShimmerContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(PROFILE_SHIMMER_ROOT_TAG)
                )
            } else {
                ProfileContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .testTag(PROFILE_CONTENT_ROOT_TAG),
        contentPadding = PaddingValues(bottom = dims.spacing.s24),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            ProfileHeaderSection(
                header = state.header,
                accountSummary = state.accountSummary,
                onSettingsClick = { onAction(ProfileUiAction.SettingsClicked) }
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = dims.spacing.s16),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
            ) {
                if (state.stats.isNotEmpty()) {
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

                ProfileStatusSummarySection(statusSummary = state.statusSummary)
            }
        }

        if (state.socialLinks.isNotEmpty()) {
            item {
                ProfileSocialLinksSection(
                    links = state.socialLinks,
                    modifier = Modifier.padding(horizontal = dims.spacing.s16)
                )
            }
        }

        item {
            PMText(
                text = stringResource(R.string.profile_section_recent_notifications),
                style = PMTextStyle.SectionLabel,
                modifier = Modifier.padding(start = dims.spacing.s16, end = dims.spacing.s16, top = dims.spacing.s8)
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
                Box(modifier = Modifier.padding(horizontal = dims.spacing.s16)) {
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
}

@Composable
private fun ProfileHeaderSection(
    header: ProfileHeaderUiModel,
    accountSummary: ProfileAccountSummaryUiModel,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme

    val emailText = accountSummary.email
        .takeIf { it.isNotBlank() }
        ?: stringResource(R.string.profile_email_not_specified)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorScheme.surface,
                shape = RoundedCornerShape(bottomStart = dims.radius.r16, bottomEnd = dims.radius.r16)
            )
            .padding(top = dims.spacing.s32, bottom = dims.spacing.s24, start = dims.spacing.s16, end = dims.spacing.s16)
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.profile_content_description_settings),
            tint = colors.textLabel,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(dims.spacing.s24)
                .clickable(onClick = onSettingsClick)
                .testTag(PROFILE_SETTINGS_ACTION_TAG)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.avatarLarge)
                    .clip(CircleShape)
                    .background(colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(dims.sizing.avatarIconInner)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
            ) {
                PMText(
                    text = header.username,
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                PMText(
                    text = emailText,
                    fontSize = dims.fontSize.sm,
                    color = colors.textLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (accountSummary.isPremiumActive) {
                    val until = accountSummary.premiumUntilText
                    val premiumText = if (until == null || until == "-") {
                        stringResource(R.string.profile_premium_active)
                    } else {
                        stringResource(R.string.profile_premium_until_format, until)
                    }
                    ProfileBadge(
                        text = premiumText,
                        backgroundColor = colorScheme.primary,
                        textColor = colorScheme.onPrimary
                    )
                } else {
                    ProfileBadge(
                        text = stringResource(R.string.profile_premium_inactive),
                        backgroundColor = colors.chipBg,
                        textColor = colors.textSecondary
                    )
                }

                if (accountSummary.joinedAtText.isNotBlank() && accountSummary.joinedAtText != "-") {
                    ProfileBadge(
                        text = accountSummary.joinedAtText,
                        backgroundColor = colors.chipBg,
                        textColor = colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileBadge(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(dims.radius.rFull))
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s4)
    ) {
        PMText(
            text = text,
            fontSize = dims.fontSize.sm,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
private fun ProfileStatusSummarySection(
    statusSummary: ProfileStatusSummaryUiModel,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(dims.spacing.s16)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            PMText(
                text = stringResource(R.string.profile_section_status_summary),
                style = PMTextStyle.SectionLabel
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                StatusCountPill(
                    label = stringResource(R.string.profile_status_approved),
                    value = statusSummary.approved.toString(),
                    dotColor = colors.success,
                    modifier = Modifier.weight(1f)
                )
                StatusCountPill(
                    label = stringResource(R.string.profile_status_pending_review),
                    value = statusSummary.pendingReview.toString(),
                    dotColor = colors.warning,
                    modifier = Modifier.weight(1f)
                )
                StatusCountPill(
                    label = stringResource(R.string.profile_status_rejected),
                    value = statusSummary.rejected.toString(),
                    dotColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatusCountPill(
    label: String,
    value: String,
    dotColor: Color,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(dims.radius.r16))
            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s8)
                .clip(CircleShape)
                .background(dotColor)
        )
        PMText(
            text = value,
            fontSize = dims.fontSize.md,
            color = colors.textPrimary
        )
        PMText(
            text = label,
            fontSize = dims.fontSize.sm,
            color = colors.textLabel,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProfileSocialLinksSection(
    links: List<ProfileSocialLinkUiModel>,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme
    val uriHandler = LocalUriHandler.current

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)) {
            PMText(
                text = stringResource(R.string.profile_section_social_links),
                style = PMTextStyle.SectionLabel
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                links.forEach { link ->
                    val iconRes = when (link.platform.uppercase()) {
                        "INSTAGRAM" -> R.drawable.ic_instagram
                        "X", "TWITTER" -> R.drawable.ic_x
                        "FACEBOOK" -> R.drawable.ic_facebook
                        else -> R.drawable.ic_link
                    }
                    Box(
                        modifier = Modifier
                            .size(dims.sizing.plateBadgeMedium)
                            .background(colorScheme.primaryContainer, CircleShape)
                            .clip(CircleShape)
                            .clickable {
                                try {
                                    uriHandler.openUri(link.url)
                                } catch (_: Exception) {
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = link.platform,
                            tint = colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(dims.sizing.iconLg)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlateReviewActivityCard(
    item: PlateReviewNotificationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        PlateCard(
            plateCode = item.plateCode,
            onClick = onClick,
            density = PlateCardDensity.Standard,
            ratingAverage = item.ratingAverage,
            commentCount = item.commentCount
        )
        StatusPill(status = item.reviewStatus, createdAtText = item.createdAtText)
    }
}

@Composable
private fun FriendRequestActivityCard(
    item: FriendRequestNotificationItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        padding = PaddingValues(horizontal = dims.spacing.s16, vertical = dims.spacing.s16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = stringResource(R.string.profile_friend_request_title, item.username),
                    fontSize = dims.fontSize.md,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = colors.textLabel,
                    modifier = Modifier.size(dims.spacing.s16)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PMText(
                    text = item.statusCode,
                    fontSize = dims.fontSize.sm,
                    color = MaterialTheme.colorScheme.primary
                )
                PMText(
                    text = item.createdAtText,
                    fontSize = dims.fontSize.sm,
                    color = colors.textLabel
                )
            }
        }
    }
}

@Composable
private fun StatusPill(
    status: ProfileReviewStatusUi,
    createdAtText: String,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val style = remember(status) {
        when (status) {
            ProfileReviewStatusUi.APPROVED -> StatusPillStyle(
                label = R.string.profile_review_status_approved,
                background = colors.categoryGreenBg,
                foreground = colors.categoryGreenFg
            )
            ProfileReviewStatusUi.PENDING_REVIEW -> StatusPillStyle(
                label = R.string.profile_review_status_pending_review,
                background = Color(0xFFFFFBEB),
                foreground = Color(0xFF92400E)
            )
            ProfileReviewStatusUi.REJECTED -> StatusPillStyle(
                label = R.string.profile_review_status_rejected,
                background = colors.errorContainer,
                foreground = Color(0xFF991B1B)
            )
            ProfileReviewStatusUi.REMOVED_BY_USER -> StatusPillStyle(
                label = R.string.profile_review_status_removed_by_user,
                background = Color(0xFFF5F3FF),
                foreground = Color(0xFF5B21B6)
            )
            ProfileReviewStatusUi.REMOVED_BY_MODERATOR -> StatusPillStyle(
                label = R.string.profile_review_status_removed_by_moderator,
                background = Color(0xFFF5F3FF),
                foreground = Color(0xFF5B21B6)
            )
            ProfileReviewStatusUi.REMOVED_BY_LEGAL_REQUEST -> StatusPillStyle(
                label = R.string.profile_review_status_removed_by_legal_request,
                background = Color(0xFFF5F3FF),
                foreground = Color(0xFF5B21B6)
            )
            ProfileReviewStatusUi.UNKNOWN -> StatusPillStyle(
                label = R.string.profile_review_status_unknown,
                background = colors.chipBg,
                foreground = colors.textSecondary
            )
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(style.background, RoundedCornerShape(dims.radius.rFull))
                .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s4)
        ) {
            PMText(
                text = stringResource(style.label),
                fontSize = dims.fontSize.sm,
                fontWeight = FontWeight.Medium,
                color = style.foreground
            )
        }
        PMText(
            text = createdAtText,
            fontSize = dims.fontSize.sm,
            color = colors.textLabel
        )
    }
}

@Composable
private fun ProfileShimmerContent(
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val colorScheme = MaterialTheme.colorScheme

    val shimmerTheme = remember(colorScheme) {
        defaultShimmerTheme.copy(
            shaderColors = listOf(
                colors.skeleton.copy(alpha = 0.55f),
                colorScheme.surface.copy(alpha = 0.95f),
                colors.skeletonSecondary.copy(alpha = 0.45f)
            ),
            shaderColorStops = listOf(0f, 0.5f, 1f)
        )
    }
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View, theme = shimmerTheme)

    LazyColumn(
        modifier = modifier.background(colorScheme.background),
        contentPadding = PaddingValues(bottom = dims.spacing.s24),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s16)
    ) {
        item {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s16)
                    .height(260.dp),
                shape = RoundedCornerShape(bottomStart = dims.radius.r16, bottomEnd = dims.radius.r16)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s16),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                repeat(3) {
                    ShimmerBlock(
                        shimmer = shimmer,
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        shape = RoundedCornerShape(dims.radius.r16)
                    )
                }
            }
        }

        items(3) {
            ShimmerBlock(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = dims.spacing.s16),
                shape = RoundedCornerShape(dims.radius.r16)
            )
        }
    }
}

@Composable
private fun ShimmerBlock(
    shimmer: com.valentinilk.shimmer.Shimmer,
    modifier: Modifier,
    shape: RoundedCornerShape
) {
    val colors = MaterialTheme.pmColors
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(
                color = colors.skeleton.copy(alpha = 0.75f),
                shape = shape
            )
    )
}

private data class StatusPillStyle(
    val label: Int,
    val background: Color,
    val foreground: Color
)

private const val PROFILE_SHIMMER_ROOT_TAG = "profile_shimmer_root"
private const val PROFILE_CONTENT_ROOT_TAG = "profile_content_root"
private const val PROFILE_SETTINGS_ACTION_TAG = "profile_settings_action"

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
