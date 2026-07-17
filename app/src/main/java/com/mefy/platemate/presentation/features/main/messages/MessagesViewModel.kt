package com.mefy.platemate.presentation.features.main.messages

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.data.local.NotificationPermissionStore
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.usecase.chat.LeaveChatUseCase
import com.mefy.platemate.domain.usecase.chat.MarkMessagesAsReadUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveChatRoomsUseCase
import com.mefy.platemate.domain.usecase.chat.SyncChatRoomsUseCase
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val observeChatRoomsUseCase: ObserveChatRoomsUseCase,
    private val syncChatRoomsUseCase: SyncChatRoomsUseCase,
    private val leaveChatUseCase: LeaveChatUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val notificationPermissionStore: NotificationPermissionStore,
    private val sessionStore: SessionStore,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MessagesUiEffect>()
    val uiEffect: SharedFlow<MessagesUiEffect> = _uiEffect.asSharedFlow()

    init {
        observeConversations()
        refreshConversations()
    }

    fun onAction(action: MessagesUiAction) {
        when (action) {
            is MessagesUiAction.ConversationClicked -> onConversationClicked(action.roomId)
            MessagesUiAction.RetryClicked -> refreshConversations()
            is MessagesUiAction.DeleteSwiped -> _uiState.update { it.copy(pendingDeleteRoomId = action.roomId) }
            MessagesUiAction.DeleteDismissed -> _uiState.update { it.copy(pendingDeleteRoomId = null) }
            MessagesUiAction.DeleteConfirmed -> onDeleteConfirmed()
            is MessagesUiAction.MarkReadSwiped -> onMarkReadSwiped(action.roomId)
        }
    }

    // Non-destructive → fires instantly, no confirm. Repo clears the local unread count optimistically
    // so the badge disappears through observeChatRoomsUseCase(); only surface an error if it fails.
    private fun onMarkReadSwiped(roomId: Long) {
        launch {
            when (val result = markMessagesAsReadUseCase(roomId)) {
                is AppResult.Success -> Unit
                is AppResult.Error -> showError(result.error.toUiText())
            }
        }
    }

    private fun onDeleteConfirmed() {
        val roomId = _uiState.value.pendingDeleteRoomId ?: return
        _uiState.update { it.copy(isDeleting = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isDeleting = false, pendingDeleteRoomId = null) }
            handleError(error)
        }) {
            when (val result = leaveChatUseCase(roomId)) {
                // Room-Flow-driven list updates itself once the repository clears the local cache row.
                is AppResult.Success -> _uiState.update { it.copy(isDeleting = false, pendingDeleteRoomId = null) }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isDeleting = false, pendingDeleteRoomId = null) }
                    showError(result.error.toUiText())
                }
            }
        }
    }

    /** Messages'ta bildirim izni daha önce istendi mi (kalıcı). İlk girişte bağlamsal istem için. */
    suspend fun hasRequestedNotificationPermission(): Boolean =
        notificationPermissionStore.hasRequestedOnMessages()

    /** Bildirim izni isteminin yapıldığını kalıcı olarak işaretler. */
    fun markNotificationPermissionRequested() {
        launch { notificationPermissionStore.setRequestedOnMessages() }
    }

    // Liste artık tek doğru kaynaktan (Room) reaktif beslenir → global canlı senk + gönderim echo'su
    // ile otomatik güncel kalır; konuşmadan dönünce son mesaj/sıra doğru.
    private fun observeConversations() {
        launch {
            combine(sessionStore.session, observeChatRoomsUseCase()) { session, rooms ->
                val userId = session?.userId
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        conversations = rooms.map { room -> room.toUiModel(userId) }
                    )
                }
            }.collect()
        }
    }

    // REST'ten Room'u tazeler (ilk açılış + retry). Hata yalnız yerel liste boşken gösterilir.
    private fun refreshConversations() {
        launch {
            when (val result = syncChatRoomsUseCase()) {
                is AppResult.Success -> Unit // Liste Room Flow'undan zaten güncellenir
                is AppResult.Error -> _uiState.update { state ->
                    // Yerelde önbellek varsa kullanıcıyı bozma; yalnız boşsa hatayı göster.
                    if (state.conversations.isEmpty()) {
                        state.copy(isLoading = false, errorMessage = result.error.toUiText())
                    } else state
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
                    participantName = conversation.name
                )
            )
        }
    }

    private fun ChatRoom.toUiModel(currentUserId: Long?): MessageConversationUiModel {
        val displayName = otherParticipantName ?: roomName ?: "Chat"
        val isSentByMe = currentUserId != null && lastMessageSenderId == currentUserId

        return MessageConversationUiModel(
            roomId = id,
            name = displayName,
            preview = lastMessageContent ?: "",
            time = lastMessageAt?.iso8601?.substringAfter("T")?.take(5) ?: "",
            unreadCount = unreadCount,
            isSentByMe = isSentByMe
        )
    }
}
