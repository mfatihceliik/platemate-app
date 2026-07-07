package com.mefy.platemate.presentation.features.main.messages

sealed interface MessagesUiAction {
    data class ConversationClicked(val roomId: Long) : MessagesUiAction
    data object RetryClicked : MessagesUiAction
    data class DeleteSwiped(val roomId: Long) : MessagesUiAction
    data object DeleteConfirmed : MessagesUiAction
    data object DeleteDismissed : MessagesUiAction
    data class MarkReadSwiped(val roomId: Long) : MessagesUiAction
}
