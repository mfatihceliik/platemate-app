package com.mefy.platemate.presentation.features.main.profile.mapper

import com.mefy.platemate.presentation.features.uimodel.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatUiModel
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel

data class ProfileUiData(
    val header: ProfileHeaderUiModel,
    val accountSummary: ProfileAccountSummaryUiModel,
    val stats: List<ProfileStatUiModel>,
    val statusSummary: ProfileStatusSummaryUiModel,
    val socialLinks: List<ProfileSocialLinkUiModel>,
    val activities: List<ProfileActivityUiModel>
)
