package com.mefy.platemate.presentation.components.model

sealed interface PlateCardAction {
    data object None : PlateCardAction
    data class Closable(val onClose: () -> Unit) : PlateCardAction
    data class Bookmarkable(
        val isBookmarked: Boolean,
        val onBookmark: () -> Unit,
        val testTag: String? = null
    ) : PlateCardAction
    data class ClosableAndBookmarkable(
        val onClose: () -> Unit,
        val isBookmarked: Boolean,
        val onBookmark: () -> Unit,
        val bookmarkTestTag: String? = null
    ) : PlateCardAction
}