package com.mefy.platemate.presentation.features.main.platedetail.review.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMPopup
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewSubmitResult
import com.mefy.platemate.presentation.features.main.platedetail.review.ReviewUiAction
import com.mefy.platemate.presentation.theme.pmColors

@Composable
fun ReviewResultPopup(
    result: ReviewSubmitResult,
    onAction: (ReviewUiAction) -> Unit
) {
    val colors = MaterialTheme.pmColors

    when (result) {
        ReviewSubmitResult.Success -> PMPopup(
            title = stringResource(R.string.review_popup_success_title),
            message = stringResource(R.string.review_popup_success_message),
            icon = Icons.Filled.CheckCircle,
            iconTint = colors.success,
            iconContainerColor = colors.categoryGreenBg,
            primaryText = stringResource(R.string.review_popup_go_home),
            onPrimaryClick = { onAction(ReviewUiAction.GoHomeClicked) },
            secondaryText = stringResource(R.string.review_popup_rate_another),
            onSecondaryClick = { onAction(ReviewUiAction.RateAnotherClicked) },
            onDismissRequest = { onAction(ReviewUiAction.DismissResult) }
        )

        ReviewSubmitResult.Pending -> PMPopup(
            title = stringResource(R.string.review_popup_pending_title),
            message = stringResource(R.string.review_popup_pending_message),
            icon = Icons.Filled.Schedule,
            iconTint = colors.warning,
            iconContainerColor = colors.categoryOrangeBg,
            primaryText = stringResource(R.string.review_popup_go_home),
            onPrimaryClick = { onAction(ReviewUiAction.GoHomeClicked) },
            secondaryText = stringResource(R.string.review_popup_rate_another),
            onSecondaryClick = { onAction(ReviewUiAction.RateAnotherClicked) },
            onDismissRequest = { onAction(ReviewUiAction.DismissResult) }
        )

        is ReviewSubmitResult.Error -> PMPopup(
            title = stringResource(R.string.review_popup_error_title),
            message = result.message.resolve(),
            icon = Icons.Filled.Cancel,
            iconTint = colors.error,
            iconContainerColor = colors.errorContainer,
            primaryText = stringResource(R.string.review_popup_retry),
            onPrimaryClick = { onAction(ReviewUiAction.RetrySubmitClicked) },
            primaryButtonColors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            secondaryText = stringResource(R.string.review_popup_cancel),
            onSecondaryClick = { onAction(ReviewUiAction.DismissResult) },
            onDismissRequest = { onAction(ReviewUiAction.DismissResult) }
        )
    }
}