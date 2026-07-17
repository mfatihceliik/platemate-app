package com.mefy.platemate.presentation.components.util

import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.uimodel.FriendRequestStatusUi
import com.mefy.platemate.presentation.features.uimodel.StatusPillStyle
import com.mefy.platemate.presentation.theme.PMColors

internal fun friendRequestStatusStyle(
    status: FriendRequestStatusUi,
    colors: PMColors
): StatusPillStyle = when (status) {
    FriendRequestStatusUi.REQUESTED -> StatusPillStyle(
        label = R.string.friend_request_status_requested,
        background = colors.categoryOrangeBg,
        foreground = colors.categoryOrangeFg
    )
    FriendRequestStatusUi.ACCEPTED -> StatusPillStyle(
        label = R.string.friend_request_status_accepted,
        background = colors.categoryGreenBg,
        foreground = colors.categoryGreenFg
    )
    FriendRequestStatusUi.REJECTED -> StatusPillStyle(
        label = R.string.friend_request_status_rejected,
        background = colors.errorContainer,
        foreground = colors.onErrorContainer
    )
    FriendRequestStatusUi.REMOVED -> StatusPillStyle(
        label = R.string.friend_request_status_removed,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    FriendRequestStatusUi.UNKNOWN -> StatusPillStyle(
        label = R.string.friend_request_status_unknown,
        background = colors.surfaceVariant,
        foreground = colors.textSecondary
    )
}
