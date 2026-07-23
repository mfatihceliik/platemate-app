package com.mefy.platemate.presentation.features.main.profile.userprofile

import com.mefy.platemate.presentation.features.uimodel.ReportReason

sealed interface UserProfileUiAction {
    data object BackClicked : UserProfileUiAction
    data object AddFriendClicked : UserProfileUiAction
    data object CancelRequestClicked : UserProfileUiAction
    data object AcceptRequestClicked : UserProfileUiAction
    data object RemoveFriendClicked : UserProfileUiAction
    data object RemoveFriendConfirmed : UserProfileUiAction
    data object RemoveFriendDismissed : UserProfileUiAction
    data object MessageClicked : UserProfileUiAction
    data object ShareClicked : UserProfileUiAction
    data object FollowersClicked : UserProfileUiAction
    data object FollowingClicked : UserProfileUiAction
    data object ReportMenuClicked : UserProfileUiAction
    data class ReportReasonSelected(val reason: ReportReason) : UserProfileUiAction
    data class ReportOtherReasonTextChanged(val text: String) : UserProfileUiAction
    data object ReportSubmitClicked : UserProfileUiAction
    data object ReportDismissed : UserProfileUiAction
}
