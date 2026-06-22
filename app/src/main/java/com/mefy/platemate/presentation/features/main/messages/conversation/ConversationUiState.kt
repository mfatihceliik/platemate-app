package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.common.text.UiText

sealed interface ConversationListItem {
    @Immutable
    data class DateHeader(val label: String) : ConversationListItem

    @Immutable
    data class Message(val model: ChatMessageUiModel) : ConversationListItem
}

@Immutable
data class ChatMessageUiModel(
    val id: Long,
    val content: String,
    val time: String,
    val isMine: Boolean,
    val isRead: Boolean
)

@Immutable
data class ConversationUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val participantName: String = "",
    val initials: String = "",
    val avatarBg: Color = Color(0xFFEEF2FF),
    val avatarFg: Color = Color(0xFF4F46E5),
    val items: List<ConversationListItem> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false
)
