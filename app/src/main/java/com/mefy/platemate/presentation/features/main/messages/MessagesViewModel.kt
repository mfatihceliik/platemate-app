package com.mefy.platemate.presentation.features.main.messages

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.data.local.NotificationPermissionStore
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoom
import com.mefy.platemate.domain.usecase.chat.LeaveChatUseCase
import com.mefy.platemate.domain.usecase.chat.MarkMessagesAsReadUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveChatRoomsUseCase
import com.mefy.platemate.domain.usecase.chat.SearchChatMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.SyncChatRoomsUseCase
import com.mefy.platemate.presentation.common.dialog.DialogFactory
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val MESSAGE_SEARCH_DEBOUNCE_MS = 300L
private const val MIN_MESSAGE_SEARCH_QUERY_LENGTH = 2

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val observeChatRoomsUseCase: ObserveChatRoomsUseCase,
    private val syncChatRoomsUseCase: SyncChatRoomsUseCase,
    private val leaveChatUseCase: LeaveChatUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val searchChatMessagesUseCase: SearchChatMessagesUseCase,
    private val notificationPermissionStore: NotificationPermissionStore,
    private val sessionStore: SessionStore,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MessagesUiEffect>()
    val uiEffect: SharedFlow<MessagesUiEffect> = _uiEffect.asSharedFlow()

    private val _searchQueryFlow = MutableStateFlow("")

    init {
        observeConversations()
        refreshConversations()
        observeMessageSearch()
    }

    fun onAction(action: MessagesUiAction) {
        when (action) {
            is MessagesUiAction.ConversationClicked -> onConversationClicked(action.roomId)
            is MessagesUiAction.DeleteSwiped -> showDialog(
                DialogFactory.deleteChatConfirmDialog(onConfirm = { onDeleteConfirmed(action.roomId) })
            )
            is MessagesUiAction.MarkReadSwiped -> onMarkReadSwiped(action.roomId)
            is MessagesUiAction.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = action.query) }
                _searchQueryFlow.value = action.query
            }
            is MessagesUiAction.MessageSearchResultClicked -> onMessageSearchResultClicked(action.result)
        }
    }

    override fun onRetry() {
        refreshConversations()
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

    private fun onDeleteConfirmed(roomId: Long) {
        _uiState.update { it.copy(isDeleting = true) }
        launch(onError = { error ->
            _uiState.update { it.copy(isDeleting = false) }
            handleError(error)
        }) {
            when (val result = leaveChatUseCase(roomId)) {
                // Room-Flow-driven list updates itself once the repository clears the local cache row.
                is AppResult.Success -> _uiState.update { it.copy(isDeleting = false) }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isDeleting = false) }
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

    // Mesaj içeriğinde arama: yazarken her tuşta IO'ya gitmemek için debounce edilir, kısa
    // sorgular (< MIN_MESSAGE_SEARCH_QUERY_LENGTH) hiç tetiklenmez. Tekil sorgu hatası
    // (runCatching) akışı asla kesmez — bir sonraki karakterde arama yine çalışmaya devam eder.
    @OptIn(FlowPreview::class)
    private fun observeMessageSearch() {
        launch {
            _searchQueryFlow
                .debounce(MESSAGE_SEARCH_DEBOUNCE_MS.milliseconds)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    val trimmed = query.trim()
                    if (trimmed.length < MIN_MESSAGE_SEARCH_QUERY_LENGTH) {
                        flowOf(emptyList())
                    } else {
                        flow { emit(runCatching { searchChatMessagesUseCase(trimmed) }.getOrDefault(emptyList())) }
                            .onStart { _uiState.update { it.copy(isSearchingMessages = true) } }
                    }
                }
                .collect { messages ->
                    _uiState.update { state ->
                        state.copy(
                            messageSearchResults = messages.toUiModels(state.conversations),
                            isSearchingMessages = false
                        )
                    }
                }
        }
    }

    private fun onMessageSearchResultClicked(result: MessageSearchResultUiModel) {
        launch {
            _uiEffect.emit(
                MessagesUiEffect.NavigateToChat(
                    conversationId = result.roomId.toString(),
                    participantName = result.participantName,
                    messageId = result.messageId
                )
            )
        }
    }

    // Bulunan mesajların odası yerelde henüz senkronize değilse (hiç açılmamış konuşma) o eşleşme
    // atlanır — bkz. searchMessages() dokümantasyonu.
    private fun List<ChatMessage>.toUiModels(conversations: List<MessageConversationUiModel>): List<MessageSearchResultUiModel> {
        val conversationsByRoomId = conversations.associateBy { it.roomId }
        return mapNotNull { message ->
            val roomId = message.chatRoomId ?: return@mapNotNull null
            val conversation = conversationsByRoomId[roomId] ?: return@mapNotNull null
            MessageSearchResultUiModel(
                messageId = message.id,
                roomId = roomId,
                participantName = conversation.name,
                content = message.content,
                time = message.sentAt?.iso8601?.substringAfter("T")?.take(5) ?: ""
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
