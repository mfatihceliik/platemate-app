package com.mefy.platemate.presentation.features.main.profile.userprofile

import com.mefy.platemate.presentation.features.uimodel.ReportReason

sealed interface UserProfileUiAction {
    data object BackClicked : UserProfileUiAction
    data object FollowClicked : UserProfileUiAction
    data object MessageClicked : UserProfileUiAction
    data object ShareClicked : UserProfileUiAction
    data object ReportMenuClicked : UserProfileUiAction
    data class ReportReasonSelected(val reason: ReportReason) : UserProfileUiAction
    data object ReportSubmitClicked : UserProfileUiAction
    data object ReportDismissed : UserProfileUiAction
}
