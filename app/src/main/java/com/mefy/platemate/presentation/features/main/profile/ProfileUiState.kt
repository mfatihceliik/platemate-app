package com.mefy.platemate.presentation.features.main.profile

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.features.main.profile.model.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatusSummaryUiModel

@Immutable
data class ProfileUiState(
    val isInitialLoading: Boolean = true,
    val header: ProfileHeaderUiModel = ProfileHeaderUiModel(),
    val accountSummary: ProfileAccountSummaryUiModel = ProfileAccountSummaryUiModel(),
    val stats: List<ProfileStatUiModel> = emptyList(),
    val statusSummary: ProfileStatusSummaryUiModel = ProfileStatusSummaryUiModel(),
    val activities: List<ProfileActivityUiModel> = emptyList(),
    val socialLinks: List<ProfileSocialLinkUiModel> = emptyList()
)
