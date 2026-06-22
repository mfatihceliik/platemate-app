package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mefy.platemate.R
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.chat.GetRoomMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.MarkMessagesAsReadUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.SendChatMessageUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.ChatDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

@HiltViewModel
class ConversationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRoomMessagesUseCase: GetRoomMessagesUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: ChatDestination = savedStateHandle.toRoute()
    private val roomId: Long = route.conversationId.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(
        ConversationUiState(
            participantName = route.participantName,
            initials = route.initials,
            avatarBg = Color(route.avatarBgArgb.toInt()),
            avatarFg = Color(route.avatarFgArgb.toInt())
        )
    )
    val uiState: StateFlow<ConversationUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ConversationUiEffect>()
    val uiEffect: SharedFlow<ConversationUiEffect> = _uiEffect.asSharedFlow()

    private var currentUsername: String = ""

    init {
        launch {
            currentUsername = observeSessionUseCase().first()?.username ?: ""
            loadMessages()
            observeRealtime()
        }
    }

    fun onAction(action: ConversationUiAction) {
        when (action) {
            is ConversationUiAction.InputChanged ->
                _uiState.update { it.copy(inputText = action.text) }

            ConversationUiAction.SendClicked -> sendMessage()

            ConversationUiAction.InfoClicked -> navigateToDetail()

            ConversationUiAction.BackClicked ->
                _uiEffect.emitUiEffect(ConversationUiEffect.NavigateBack)

            ConversationUiAction.RetryClicked -> loadMessages()
        }
    }

    private fun loadMessages() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = { e ->
            _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            handleError(e)
        }) {
            when (val result = getRoomMessagesUseCase(roomId)) {
                is AppResult.Success -> {
                    val items = buildListItems(result.data)
                    _uiState.update { it.copy(isLoading = false, errorMessage = null, items = items) }
                    markMessagesAsReadUseCase(roomId)
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.error.toUiText()) }
                }
            }
        }
    }

    private fun observeRealtime() {
        launch {
            observeMessagesUseCase(roomId).collect { incoming ->
                _uiState.update { current ->
                    val existingIds = current.items
                        .filterIsInstance<ConversationListItem.Message>()
                        .map { it.model.id }
                        .toSet()
                    if (incoming.id in existingIds) return@update current

                    val allMessages = current.items
                        .filterIsInstance<ConversationListItem.Message>()
                        .map { it.model }
                        .map { it.toDomain() } + incoming

                    current.copy(items = buildListItems(allMessages))
                }
                if (!incoming.isRead && incoming.senderUsername != currentUsername) {
                    markMessagesAsReadUseCase(roomId)
                }
            }
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank() || _uiState.value.isSending) return

        _uiState.update { it.copy(isSending = true, inputText = "") }
        launch(onError = { e ->
            handleError(e)
            _uiState.update { it.copy(isSending = false) }
        }) {
            sendChatMessageUseCase(roomId, text)
            _uiState.update { it.copy(isSending = false) }
        }
    }

    private fun navigateToDetail() {
        val state = _uiState.value
        _uiEffect.emitUiEffect(
            ConversationUiEffect.NavigateToChatDetail(
                conversationId = roomId.toString(),
                participantName = state.participantName,
                initials = state.initials,
                avatarBgArgb = state.avatarBg.toArgb().toLong(),
                avatarFgArgb = state.avatarFg.toArgb().toLong()
            )
        )
    }

    private fun buildListItems(messages: List<ChatMessage>): List<ConversationListItem> {
        val sorted = messages.sortedBy { it.sentAt?.iso8601 }
        val result = mutableListOf<ConversationListItem>()
        var lastDate = ""
        sorted.forEach { message ->
            val date = message.sentAt?.iso8601?.take(10) ?: ""
            if (date.isNotEmpty() && date != lastDate) {
                result.add(ConversationListItem.DateHeader(date.toRelativeDateLabel()))
                lastDate = date
            }
            result.add(ConversationListItem.Message(message.toUiModel()))
        }
        return result
    }

    private fun ChatMessage.toUiModel() = ChatMessageUiModel(
        id = id,
        content = content,
        time = sentAt?.iso8601?.substringAfter("T")?.take(5) ?: "",
        isMine = senderUsername == currentUsername,
        isRead = isRead
    )

    private fun ChatMessageUiModel.toDomain() = ChatMessage(
        id = id,
        chatRoomId = roomId,
        senderUsername = if (isMine) currentUsername else "",
        content = content,
        sentAt = null,
        isRead = isRead
    )
}

private fun String.toRelativeDateLabel(): String {
    if (isEmpty()) return ""
    val cal = Calendar.getInstance()
    val today = "%04d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
    cal.add(Calendar.DAY_OF_MONTH, -1)
    val yesterday = "%04d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH)
    )
    return when (this) {
        today -> "Bugün"
        yesterday -> "Dün"
        else -> this
    }
}
