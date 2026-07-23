package com.mefy.platemate.presentation.components.util

import com.mefy.platemate.R
import com.mefy.platemate.presentation.features.uimodel.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.uimodel.StatusPillStyle
import com.mefy.platemate.presentation.theme.colors.PMColors

internal fun reviewStatusStyle(
    status: ProfileReviewStatusUi,
    colors: PMColors
): StatusPillStyle = when (status) {
    ProfileReviewStatusUi.APPROVED -> StatusPillStyle(
        label = R.string.profile_review_status_approved,
        background = colors.categoryGreenBg,
        foreground = colors.categoryGreenFg
    )
    ProfileReviewStatusUi.PENDING_REVIEW -> StatusPillStyle(
        label = R.string.profile_review_status_pending_review,
        background = colors.categoryOrangeBg,
        foreground = colors.categoryOrangeFg
    )
    ProfileReviewStatusUi.REJECTED -> StatusPillStyle(
        label = R.string.profile_review_status_rejected,
        background = colors.errorContainer,
        foreground = colors.onErrorContainer
    )
    ProfileReviewStatusUi.REMOVED_BY_USER -> StatusPillStyle(
        label = R.string.profile_review_status_removed_by_user,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    ProfileReviewStatusUi.REMOVED_BY_MODERATOR -> StatusPillStyle(
        label = R.string.profile_review_status_removed_by_moderator,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    ProfileReviewStatusUi.REMOVED_BY_LEGAL_REQUEST -> StatusPillStyle(
        label = R.string.profile_review_status_removed_by_legal_request,
        background = colors.categoryIndigoBg,
        foreground = colors.categoryIndigoFg
    )
    ProfileReviewStatusUi.UNKNOWN -> StatusPillStyle(
        label = R.string.profile_review_status_unknown,
        background = colors.surfaceVariant,
        foreground = colors.textSecondary
    )
}