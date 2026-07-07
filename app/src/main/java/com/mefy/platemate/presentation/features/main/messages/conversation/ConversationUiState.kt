package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.common.text.UiText

sealed interface ConversationListItem {
    /** Day boundary as a stable `yyyy-MM-dd`; the UI resolves it to a localized label. */
    @Immutable
    data class DateHeader(val isoDate: String) : ConversationListItem

    @Immutable
    data class Message(val model: ChatMessageUiModel) : ConversationListItem

    /** WhatsApp tarzı "Okunmamış mesajlar" ayracı; oda açıldığı anki okunmamış sayısını taşır. */
    @Immutable
    data class UnreadDivider(val count: Int) : ConversationListItem
}

@Immutable
data class ChatMessageUiModel(
    val id: Long,
    val content: String,
    val time: String,
    val isMine: Boolean,
    val status: MessageStatus
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
    // items içindeki UnreadDivider'ın index'i; ilk açılışta liste buraya kaydırılır.
    val firstUnreadIndex: Int? = null,
    val inputText: String = "",
    val isSending: Boolean = false,
    // Other participant's online status; null = unknown (no data yet / not visible).
    val isOtherUserOnline: Boolean? = null,
    // Incoming message request awaiting this user's approval
    val isIncomingRequest: Boolean = false,
    val isRespondingRequest: Boolean = false
)
