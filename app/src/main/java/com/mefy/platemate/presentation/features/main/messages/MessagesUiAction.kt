package com.mefy.platemate.presentation.features.main.messages

sealed interface MessagesUiAction {
    data class ConversationClicked(val roomId: Long) : MessagesUiAction
    data class DeleteSwiped(val roomId: Long) : MessagesUiAction
    data class MarkReadSwiped(val roomId: Long) : MessagesUiAction
    data class SearchQueryChanged(val query: String) : MessagesUiAction
    data class MessageSearchResultClicked(val result: MessageSearchResultUiModel) : MessagesUiAction
}
