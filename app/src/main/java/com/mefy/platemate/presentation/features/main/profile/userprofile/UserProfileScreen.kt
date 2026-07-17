package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.ReportUserBottomSheet
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileActionButtons
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileHeaderCard
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileReviewCard
import com.mefy.platemate.presentation.features.main.profile.userprofile.components.UserProfileSocialLinks
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun UserProfileScreen(
    modifier: Modifier = Modifier,
    state: UserProfileUiState,
    onAction: (UserProfileUiAction) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val onAddFriendClicked = remember(onAction) { { onAction(UserProfileUiAction.AddFriendClicked) } }
    val onCancelRequestClicked = remember(onAction) { { onAction(UserProfileUiAction.CancelRequestClicked) } }
    val onAcceptRequestClicked = remember(onAction) { { onAction(UserProfileUiAction.AcceptRequestClicked) } }
    val onRemoveFriendClicked = remember(onAction) { { onAction(UserProfileUiAction.RemoveFriendClicked) } }
    
    val onMessageClicked = remember(onAction) { { onAction(UserProfileUiAction.MessageClicked) } }
    val onShareClicked = remember(onAction) { { onAction(UserProfileUiAction.ShareClicked) } }
    val onReportReasonSelected = remember(onAction) { { reason: ReportReason -> onAction(UserProfileUiAction.ReportReasonSelected(reason)) } }
    val onReportSubmitClicked = remember(onAction) { { onAction(UserProfileUiAction.ReportSubmitClicked) } }
    val onReportDismissed = remember(onAction) { { onAction(UserProfileUiAction.ReportDismissed) } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.surface)
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        item(key = "header") {
            UserProfileHeaderCard(
                displayName = state.displayName,
                username = state.username,
                bio = state.bio,
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
                    friendshipStatus = state.friendshipStatus,
                    onAddFriendClick = onAddFriendClicked,
                    onCancelRequestClick = onCancelRequestClicked,
                    onAcceptRequestClick = onAcceptRequestClicked,
                    onRemoveFriendClick = onRemoveFriendClicked,
                    onMessageClick = onMessageClicked,
                    onShareClick = onShareClicked
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
                )
            }

            items(
                items = state.approvedReviews,
                key = { "review_${it.id}" }
            ) { review ->
                UserProfileReviewCard(
                    review = review,
                )
            }
        }
    }

    if (state.showRemoveFriendPopup) {
        PMPopup(
            title = stringResource(id = R.string.user_profile_remove_friend_title),
            message = stringResource(id = R.string.user_profile_remove_friend_desc),
            primaryText = stringResource(id = R.string.user_profile_remove),
            secondaryText = stringResource(id = R.string.common_cancel),
            onPrimaryClick = { onAction(UserProfileUiAction.RemoveFriendConfirmed) },
            onSecondaryClick = { onAction(UserProfileUiAction.RemoveFriendDismissed) },
            onDismissRequest = { onAction(UserProfileUiAction.RemoveFriendDismissed) },
            primaryButtonColors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.pmColors.error,
                contentColor = MaterialTheme.pmColors.onError
            )
        )
    }

    if (state.showReportSheet) {
        ReportUserBottomSheet(
            participantName = state.displayName,
            username = state.username,
            selectedReason = state.selectedReportReason,
            onReasonSelected = { onReportReasonSelected(it) },
            otherReasonText = state.otherReportReasonText,
            onOtherReasonTextChanged = { onAction(UserProfileUiAction.ReportOtherReasonTextChanged(it)) },
            onDismiss = onReportDismissed,
            onSubmit = onReportSubmitClicked
        )
    }
}



@Preview(name = "UserProfileScreen Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UserProfileScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UserProfileScreen(
            state = previewState,
            onAction = {},
        )
    }
}

@Preview(name = "UserProfileScreen Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UserProfileScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UserProfileScreen(
            state = previewState,
            onAction = {},
        )
    }
}
private val previewState = UserProfileUiState(
    isLoading = false,
    userId = "user_preview",
    displayName = "Ahmet Yılmaz",
    username = "@ahmetyilmaz",
    bio = "İstanbul sürücüsü. Saygılı ve temkinli araç kullanırım.",
    isVerified = true,
    isOnline = true,
    friendshipStatus = "NONE",
    friendshipId = null,
    reviewCount = 47,
    followerCount = "1.2K",
    followingCount = 89,
    socialLinks = listOf(
        ProfileSocialLinkUiModel(
            id = 1,
            platform = "INSTAGRAM",
            url = "https://instagram.com/ahmet",
            iconUrl = null,
            backgroundColor = Color(0xFFFDF2F8),
            iconTint = Color(0xFFDB2777)
        ),
        ProfileSocialLinkUiModel(
            id = 2,
            platform = "X",
            url = "https://x.com/ahmet",
            iconUrl = null,
            backgroundColor = Color(0xFFF1F5F9),
            iconTint = Color(0xFF0F172A)
        )
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
