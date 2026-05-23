package com.mefy.platemate.presentation.common.event

import com.mefy.platemate.presentation.common.text.UiText

data class CommonDialogModel(
    val title: UiText,
    val message: UiText,
    val confirmText: UiText,
    val dismissText: UiText? = null,
    val dismissible: Boolean = true
)
