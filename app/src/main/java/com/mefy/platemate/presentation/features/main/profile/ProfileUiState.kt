package com.mefy.platemate.presentation.features.main.profile

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel

@Immutable
data class ProfileUiState(
    val isInitialLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: UiText? = null,
    val header: ProfileHeaderUiModel = ProfileHeaderUiModel(),
    val accountSummary: ProfileAccountSummaryUiModel = ProfileAccountSummaryUiModel(),
    val stats: List<ProfileStatUiModel> = emptyList(),
    val statusSummary: ProfileStatusSummaryUiModel = ProfileStatusSummaryUiModel(),
    val activities: List<ProfileActivityUiModel> = emptyList(),
    val socialLinks: List<ProfileSocialLinkUiModel> = emptyList()
)
