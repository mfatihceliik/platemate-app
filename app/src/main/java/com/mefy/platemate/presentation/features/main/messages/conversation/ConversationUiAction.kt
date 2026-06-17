package com.mefy.platemate.presentation.features.main.messages.conversation

sealed interface ConversationUiAction {
    data class InputChanged(val text: String) : ConversationUiAction
    data object SendClicked : ConversationUiAction
    data object InfoClicked : ConversationUiAction
    data object BackClicked : ConversationUiAction
}
