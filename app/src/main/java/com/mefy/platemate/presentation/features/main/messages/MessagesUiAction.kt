package com.mefy.platemate.presentation.features.main.messages

sealed interface MessagesUiAction {
    data class ConversationClicked(val roomId: Long) : MessagesUiAction
}
