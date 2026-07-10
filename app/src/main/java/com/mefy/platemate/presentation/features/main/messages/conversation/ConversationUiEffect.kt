package com.mefy.platemate.presentation.features.main.messages.conversation

sealed interface ConversationUiEffect {
    data object NavigateBack : ConversationUiEffect
    data class NavigateToChatDetail(
        val conversationId: String,
        val participantName: String,
    ) : ConversationUiEffect
}
