package com.mefy.platemate.presentation.features.main.platedetail

import com.mefy.platemate.presentation.features.main.platedetail.model.CommentReportReason

sealed interface PlateDetailUiAction {
    data object BackClicked : PlateDetailUiAction
    data object MenuClicked : PlateDetailUiAction
    data object ReviewClicked : PlateDetailUiAction
    data object RetryClicked : PlateDetailUiAction
    data class AvatarClicked(val userId: Long) : PlateDetailUiAction
    data class EditReviewClicked(val reviewId: Long) : PlateDetailUiAction
    data class ReportReviewClicked(val reviewId: Long) : PlateDetailUiAction
    data class ReportReasonSelected(val reason: CommentReportReason) : PlateDetailUiAction
    data class ReportDescriptionChanged(val description: String) : PlateDetailUiAction
    data object ReportSubmitClicked : PlateDetailUiAction
    data object ReportDismissed : PlateDetailUiAction
}
