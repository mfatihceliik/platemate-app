package com.mefy.platemate.presentation.common.dialog

import com.mefy.platemate.presentation.common.text.UiText

/** [com.mefy.platemate.presentation.components.PMPopup] görselini (ikon + renk) belirleyen anlamsal varyant. */
enum class DialogVariant { Info, Success, Error }

data class DialogModel(
    val title: UiText,
    val message: UiText,
    val confirmText: UiText,
    val dismissText: UiText? = null,
    val dismissible: Boolean = true,
    val variant: DialogVariant = DialogVariant.Info,
    val onConfirm: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null
)
