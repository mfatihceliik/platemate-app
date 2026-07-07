package com.mefy.platemate.presentation.features.main.platedetail.review

import com.mefy.platemate.presentation.common.text.UiText

sealed interface ReviewSubmitResult {
    data object Success : ReviewSubmitResult
    data object Pending : ReviewSubmitResult
    data class Error(val message: UiText) : ReviewSubmitResult
}