package com.mefy.platemate.presentation.features.main.review

sealed interface ReviewUiAction {
    data object BackClicked : ReviewUiAction
    data class OverallRatingChanged(val rating: Int) : ReviewUiAction
    data class SubRatingChanged(val key: String, val rating: Int) : ReviewUiAction
    data class TagToggled(val code: String) : ReviewUiAction
    data class CommentChanged(val text: String) : ReviewUiAction
    data class AnonymousToggled(val checked: Boolean) : ReviewUiAction
    data object SubmitClicked : ReviewUiAction
}
