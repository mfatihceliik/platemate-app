package com.mefy.platemate.presentation.features.main.messages

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class MessagesUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val conversations: List<MessageConversationUiModel> = emptyList()
)

@Immutable
data class MessageConversationUiModel(
    val roomId: Long,
    val initials: String,
    val name: String,
    val preview: String,
    val time: String,
    val isUnread: Boolean,
    val avatarBg: Color,
    val avatarFg: Color
)
