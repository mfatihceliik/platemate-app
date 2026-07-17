package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ReportReason
import com.mefy.platemate.presentation.features.uimodel.UserProfileReviewUiModel

@Immutable
data class UserProfileUiState(
    val isLoading: Boolean = true,
    val userId: String = "",
    val displayName: String = "",
    val username: String = "",
    val bio: String = "",
    val isVerified: Boolean = false,
    val isOnline: Boolean = false,
    val friendshipStatus: String = "NONE",
    val friendshipId: Long? = null,
    val showRemoveFriendPopup: Boolean = false,
    val reviewCount: Int = 0,
    val followerCount: String = "0",
    val followingCount: Int = 0,
    val socialLinks: List<ProfileSocialLinkUiModel> = emptyList(),
    val approvedReviews: List<UserProfileReviewUiModel> = emptyList(),
    val showReportSheet: Boolean = false,
    val selectedReportReason: ReportReason? = null,
    val otherReportReasonText: String = "",
    val isSubmittingReport: Boolean = false
)
