package com.mefy.platemate.presentation.features.main.profile.userprofile

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.ReportReason
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileReviewUiModel
import com.mefy.platemate.presentation.features.main.profile.userprofile.model.UserProfileSocialLinkUiModel

@Immutable
data class UserProfileUiState(
    val isLoading: Boolean = true,
    val userId: String = "",
    val displayName: String = "",
    val username: String = "",
    val initials: String = "",
    val avatarBg: Color = Color(0xFFECFEFF),
    val avatarFg: Color = Color(0xFF0E7490),
    val bio: String = "",
    val isVerified: Boolean = false,
    val isOnline: Boolean = false,
    val isFollowing: Boolean = false,
    val reviewCount: Int = 0,
    val followerCount: String = "0",
    val followingCount: Int = 0,
    val socialLinks: List<UserProfileSocialLinkUiModel> = emptyList(),
    val approvedReviews: List<UserProfileReviewUiModel> = emptyList(),
    val showReportSheet: Boolean = false,
    val selectedReportReason: ReportReason? = null,
    val isSubmittingReport: Boolean = false
)
