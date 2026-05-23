package com.mefy.platemate.presentation.common.error

import com.mefy.platemate.presentation.common.text.UiText

data class ResolvedUiError(
    val message: UiText,
    val fieldErrors: Map<String, UiText> = emptyMap(),
    val uxAction: UiErrorUxAction = UiErrorUxAction.NONE
)
