package com.mefy.platemate.presentation.features.main.profile.mapper

import com.mefy.platemate.presentation.features.main.profile.model.ProfileAccountSummaryUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileActivityUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileHeaderUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileSocialLinkUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatUiModel
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatusSummaryUiModel

data class ProfileUiData(
    val header: ProfileHeaderUiModel,
    val accountSummary: ProfileAccountSummaryUiModel,
    val stats: List<ProfileStatUiModel>,
    val statusSummary: ProfileStatusSummaryUiModel,
    val socialLinks: List<ProfileSocialLinkUiModel>,
    val activities: List<ProfileActivityUiModel>
)
