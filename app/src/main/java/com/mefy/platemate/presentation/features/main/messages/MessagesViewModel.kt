package com.mefy.platemate.presentation.features.main.messages

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.usecase.chat.GetChatRoomsUseCase
import com.mefy.platemate.presentation.common.error.UiErrorResolver
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val getChatRoomsUseCase: GetChatRoomsUseCase,
    uiErrorResolver: UiErrorResolver
) : BaseViewModel(uiErrorResolver) {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MessagesUiEffect>()
    val uiEffect: SharedFlow<MessagesUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadConversations()
    }

    fun onAction(action: MessagesUiAction) {
        when (action) {
            is MessagesUiAction.ConversationClicked -> onConversationClicked(action.roomId)
        }
    }

    private fun loadConversations() {
        launch {
            when (val result = getChatRoomsUseCase()) {
                is AppResult.Success -> {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            conversations = result.data.map { it.toUiModel() }
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    handleError(result.error)
                }
            }
        }
    }

    private fun onConversationClicked(roomId: Long) {
        val conversation = _uiState.value.conversations.find { it.roomId == roomId } ?: return
        launch {
            _uiEffect.emit(
                MessagesUiEffect.NavigateToChat(
                    conversationId = roomId.toString(),
                    participantName = conversation.name,
                    initials = conversation.initials,
                    avatarBgArgb = conversation.avatarBg.toArgb().toLong(),
                    avatarFgArgb = conversation.avatarFg.toArgb().toLong()
                )
            )
        }
    }

    private fun ChatRoom.toUiModel(): MessageConversationUiModel {
        val displayName = otherParticipantName ?: roomName ?: "Chat"
        val initials = displayName.split(" ")
            .take(2)
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .joinToString("")
            .ifEmpty { "?" }

        val colorPairs = listOf(
            Color(0xFFEEF2FF) to Color(0xFF4F46E5),
            Color(0xFFECFEFF) to Color(0xFF0891B2),
            Color(0xFFF0FDF4) to Color(0xFF15803D),
            Color(0xFFFFF7ED) to Color(0xFFC2410C)
        )
        val pair = colorPairs[(id % colorPairs.size).toInt()]

        return MessageConversationUiModel(
            roomId = id,
            initials = initials,
            name = displayName,
            preview = lastMessageContent ?: "",
            time = lastMessageAt?.iso8601?.substringAfter("T")?.take(5) ?: "",
            isUnread = false,
            avatarBg = pair.first,
            avatarFg = pair.second
        )
    }
}
