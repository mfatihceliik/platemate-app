package com.mefy.platemate.presentation.features.main.messages

sealed interface MessagesUiEffect {
    data class NavigateToChat(
        val conversationId: String,
        val participantName: String,
        val messageId: Long? = null
    ) : MessagesUiEffect
}
