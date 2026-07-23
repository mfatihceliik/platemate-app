package com.mefy.platemate.presentation.features.main.messages

import androidx.compose.runtime.Immutable
import com.mefy.platemate.presentation.common.text.UiText

@Immutable
data class MessagesUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val conversations: List<MessageConversationUiModel> = emptyList(),
    val isDeleting: Boolean = false,
    val searchQuery: String = "",
    val messageSearchResults: List<MessageSearchResultUiModel> = emptyList(),
    val isSearchingMessages: Boolean = false
) {
    val filteredConversations: List<MessageConversationUiModel>
        get() = if (searchQuery.isBlank()) {
            conversations
        } else {
            conversations.filter {
                it.name.contains(searchQuery, ignoreCase = true) || it.preview.contains(searchQuery, ignoreCase = true)
            }
        }
}

@Immutable
data class MessageConversationUiModel(
    val roomId: Long,
    val name: String,
    val preview: String,
    val time: String,
    val unreadCount: Int,
    val isSentByMe: Boolean
)

@Immutable
data class MessageSearchResultUiModel(
    val messageId: Long,
    val roomId: Long,
    val participantName: String,
    val content: String,
    val time: String
)
