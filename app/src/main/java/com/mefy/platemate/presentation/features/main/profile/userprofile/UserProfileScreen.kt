package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMTopBarConfig
import com.mefy.platemate.presentation.components.PMBaseScreen
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.ReportUserBottomSheet
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileActionButtons
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileHeaderCard
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileReviewCard
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileSocialLinks
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileReviewUiModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileSocialLinkUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun UserProfileScreen(
    state: UserProfileUiState,
    onAction: (UserProfileUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBaseScreen(
        modifier = modifier,
        topBarConfig = PMTopBarConfig.Standard(
            title = stringResource(R.string.user_profile_title),
            onBackClick = { onAction(UserProfileUiAction.BackClicked) },
            actions = {
                IconButton(onClick = { onAction(UserProfileUiAction.ReportMenuClicked) }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.user_profile_more_options),
                        tint = colors.textPrimary
                    )
                }
            }
        ),
        containerColor = colors.surfaceSecondary
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { PMCircularProgressIndicator() }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = dims.spacing.s24),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s0)
            ) {
                item(key = "header") {
                    UserProfileHeaderCard(
                        displayName = state.displayName,
                        username = state.username,
                        bio = state.bio,
                        initials = state.initials,
                        avatarBg = state.avatarBg,
                        avatarFg = state.avatarFg,
                        isVerified = state.isVerified,
                        isOnline = state.isOnline,
                        reviewCount = state.reviewCount,
                        followerCount = state.followerCount,
                        followingCount = state.followingCount
                    )
                }

                item(key = "actions") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colors.surface)
                            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
                    ) {
                        UserProfileActionButtons(
                            isFollowing = state.isFollowing,
                            onFollowClick = { onAction(UserProfileUiAction.FollowClicked) },
                            onMessageClick = { onAction(UserProfileUiAction.MessageClicked) },
                            onShareClick = { onAction(UserProfileUiAction.ShareClicked) }
                        )
                        if (state.socialLinks.isNotEmpty()) {
                            Spacer(modifier = Modifier.padding(top = dims.spacing.s12))
                            UserProfileSocialLinks(
                                links = state.socialLinks,
                                onLinkClick = {}
                            )
                        }
                    }
                    HorizontalDivider(color = colors.outlineVariant)
                }

                if (state.approvedReviews.isNotEmpty()) {
                    item(key = "reviews_header") {
                        PMText(
                            text = stringResource(R.string.user_profile_approved_reviews),
                            style = PMTextStyle.Body,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colors.surfaceSecondary)
                                .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12)
                        )
                    }

                    items(
                        items = state.approvedReviews,
                        key = { "review_${it.id}" }
                    ) { review ->
                        UserProfileReviewCard(
                            review = review,
                            modifier = Modifier.padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s4)
                        )
                    }
                }
            }
        }
    }

    if (state.showReportSheet) {
        ReportUserBottomSheet(
            participantName = state.displayName,
            username = state.username,
            initials = state.initials,
            avatarBg = state.avatarBg,
            avatarFg = state.avatarFg,
            selectedReason = state.selectedReportReason,
            onReasonSelected = { onAction(UserProfileUiAction.ReportReasonSelected(it)) },
            onDismiss = { onAction(UserProfileUiAction.ReportDismissed) },
            onSubmit = { onAction(UserProfileUiAction.ReportSubmitClicked) }
        )
    }
}

private val previewState = UserProfileUiState(
    isLoading = false,
    userId = "user_preview",
    displayName = "Ahmet Yılmaz",
    username = "@ahmetyilmaz",
    initials = "AY",
    avatarBg = Color(0xFFECFEFF),
    avatarFg = Color(0xFF0E7490),
    bio = "İstanbul sürücüsü. Saygılı ve temkinli araç kullanırım.",
    isVerified = true,
    isOnline = true,
    isFollowing = false,
    reviewCount = 47,
    followerCount = "1.2K",
    followingCount = 89,
    socialLinks = listOf(
        UserProfileSocialLinkUiModel(platform = "INSTAGRAM", url = "https://instagram.com/ahmet"),
        UserProfileSocialLinkUiModel(platform = "X", url = "https://x.com/ahmet")
    ),
    approvedReviews = listOf(
        UserProfileReviewUiModel(
            id = 1L,
            plateCode = "34",
            plateNumber = "34 EK 0682",
            city = "İstanbul",
            date = "12 Haz 2025",
            rating = 4.5f,
            tags = listOf("Nazik", "Yol verdi", "Saygılı"),
            comment = "Çok nazik bir sürücü, yol verdi ve güvenli mesafe bıraktı."
        ),
        UserProfileReviewUiModel(
            id = 2L,
            plateCode = "06",
            plateNumber = "06 AB 1234",
            city = "Ankara",
            date = "3 May 2025",
            rating = 5.0f,
            tags = listOf("Dikkatli", "Sakin"),
            comment = "Mükemmel bir sürücü deneyimi."
        )
    )
)

@Preview(name = "UserProfileScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UserProfileScreen(
            state = previewState,
            onAction = {}
        )
    }
}

@Preview(name = "UserProfileScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UserProfileScreen(
            state = previewState,
            onAction = {}
        )
    }
}
