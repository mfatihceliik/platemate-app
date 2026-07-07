package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.R
import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.notification.ActiveConversationTracker
import com.mefy.platemate.presentation.common.error.toUiText
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.domain.model.chat.ChatMessage
import com.mefy.platemate.domain.model.chat.ChatRoomRequestStatus
import com.mefy.platemate.domain.usecase.auth.ObserveSessionUseCase
import com.mefy.platemate.domain.usecase.chat.AcceptChatRequestUseCase
import com.mefy.platemate.domain.usecase.chat.CreateOrGetPrivateChatRoomUseCase
import com.mefy.platemate.domain.usecase.chat.DeclineChatRequestUseCase
import com.mefy.platemate.domain.usecase.chat.GetChatRoomUseCase
import com.mefy.platemate.domain.usecase.chat.GetRoomPresenceUseCase
import com.mefy.platemate.domain.usecase.chat.JoinChatRoomUseCase
import com.mefy.platemate.domain.usecase.chat.MarkMessagesAsReadUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveChatErrorsUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveUserPresenceUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveRoomMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.SyncRoomMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.SendChatMessageUseCase
import com.mefy.platemate.presentation.common.avatar.AvatarPalette
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.navigation.ChatDestination
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getChatRoomUseCase: GetChatRoomUseCase,
    private val observeRoomMessagesUseCase: ObserveRoomMessagesUseCase,
    private val syncRoomMessagesUseCase: SyncRoomMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val joinChatRoomUseCase: JoinChatRoomUseCase,
    private val observeChatErrorsUseCase: ObserveChatErrorsUseCase,
    private val getRoomPresenceUseCase: GetRoomPresenceUseCase,
    private val observeUserPresenceUseCase: ObserveUserPresenceUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val acceptChatRequestUseCase: AcceptChatRequestUseCase,
    private val declineChatRequestUseCase: DeclineChatRequestUseCase,
    private val createOrGetPrivateChatRoomUseCase: CreateOrGetPrivateChatRoomUseCase,
    private val observeSessionUseCase: ObserveSessionUseCase,
    private val activeConversationTracker: ActiveConversationTracker,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    private val route: ChatDestination = savedStateHandle.toRoute()

    // roomId == 0 → taslak konuşma (henüz sunucuda oda yok). İlk mesaj gönderiminde
    // [otherUserId] ile oda yaratılıp buraya yazılır.
    private var roomId: Long = route.conversationId.toLongOrNull() ?: 0L
    private val otherUserId: Long = route.otherUserId

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

    private var currentUserId: Long? = null
    private var currentUsername: String = ""

    // Diğer katılımcının id'si presence filtreleme için; taslakta route'tan, gerçek odada
    // snapshot yanıtından çözümlenir.
    private var presenceUserId: Long? = otherUserId.takeIf { it > 0 }

    // Source of truth for messages in this room.
    private var messages: List<ChatMessage> = emptyList()

    // Okundu bildirimi: hangi gelen mesaj id'si için PUT atıldıysa tekrarı önlenir;
    // PUT başarısız olursa null'a çekilir ki sonraki emisyonda/resume'da yeniden denensin.
    private var lastMarkReadRequestedId: Long? = null

    // Ekran RESUMED değilken (arka plan / başka ekran) okundu gönderilmez.
    private var isResumed = false

    // "Okunmamış mesajlar" ayracı: oda açıldığındaki ilk okunmamış mesajın id'si ve sayısı,
    // ilk boş-olmayan emisyonda TEK SEFER alınır (markMessagesAsRead sonrası bayraklar
    // değişse bile ayraç ekranda sabit kalır; VM ölünce doğal temizlenir).
    private var unreadAnchorCaptured = false
    private var unreadAnchorMessageId: Long? = null
    private var unreadAnchorCount = 0

    init {
        observeErrors()
        launch {
            val session = observeSessionUseCase().first()
            currentUserId = session?.userId
            currentUsername = session?.username ?: ""
            if (roomId > 0) {
                startRoomSession()
            } else {
                // Taslak konuşma: oda yok, yükleme yok; ilk mesajla oluşacak.
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    override fun onCleared() {
        // Sadece bu ekranın işaretlediği odayı temizle; daha yeni bir ekran başka oda
        // yazmışsa onu silme.
        if (activeConversationTracker.activeRoomId == roomId) {
            activeConversationTracker.activeRoomId = null
        }
        super.onCleared()
    }

    /** Gerçek oda (roomId > 0) için yükleme + realtime gözlemcilerini başlatır. */
    private fun startRoomSession() {
        // Bu odadayken o odanın mesaj bildirimini bastır (canlı soket mesajı zaten ekranda).
        activeConversationTracker.activeRoomId = roomId
        // Soket odasına katıl ki backend'in oda yayını (new_message echo'su) bize ulaşsın.
        // Yeni oluşturulan odalar bağlantı anında mevcut olmadığından şarttır.
        launch { joinChatRoomUseCase(roomId) }
        loadRoom()
        observeMessages()
        syncMessages()
        loadPresence()
    }

    /** Diğer kullanıcının online durumunu önce REST snapshot'tan alır, sonra canlı dinler. */
    private fun loadPresence() {
        launch {
            when (val result = getRoomPresenceUseCase(roomId)) {
                is AppResult.Success -> {
                    val presence = result.data
                    presence.userId?.let { presenceUserId = it }
                    _uiState.update { it.copy(isOtherUserOnline = presence.online) }
                    presenceUserId?.let { observePresence(it) }
                }
                is AppResult.Error -> Unit // Non-fatal; presence simply stays hidden
            }
        }
    }

    private fun observePresence(userId: Long) {
        launch {
            observeUserPresenceUseCase(userId).collect { online ->
                _uiState.update { it.copy(isOtherUserOnline = online) }
            }
        }
    }

    /** Soketten gelen iş kuralı hatalarını (limit/engel/kapalı/red) snackbar ile gösterir. */
    private fun observeErrors() {
        launch {
            observeChatErrorsUseCase().collect { message ->
                _uiState.update { it.copy(isSending = false) }
                showError(UiText.Dynamic(message))
            }
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
            ConversationUiAction.RetryClicked -> syncMessages()
            ConversationUiAction.AcceptRequestClicked -> respondToRequest(accept = true)
            ConversationUiAction.DeclineRequestClicked -> respondToRequest(accept = false)
            ConversationUiAction.ScreenResumed -> {
                isResumed = true
                maybeMarkRead()
            }
            ConversationUiAction.ScreenPaused -> isResumed = false
        }
    }

    private fun loadRoom() {
        launch {
            when (val result = getChatRoomUseCase(roomId)) {
                is AppResult.Success -> {
                    val room = result.data
                    val incoming = room.requestStatus == ChatRoomRequestStatus.PENDING &&
                            room.initiatorId != null &&
                            room.initiatorId != currentUserId
                    // Başlığı otoriter olarak odadan doldur: deeplink'te nav arg'ları (FCM title)
                    // jenerik/eksik olabilir; odadan gelen ad doğru olur.
                    val displayName = room.otherParticipantName ?: room.roomName ?: "Chat"
                    val (avatarBackground, avatarForeground) = AvatarPalette.colorsFor(roomId)
                    _uiState.update {
                        it.copy(
                            isIncomingRequest = incoming,
                            participantName = displayName,
                            initials = AvatarPalette.initials(displayName),
                            avatarBg = avatarBackground,
                            avatarFg = avatarForeground
                        )
                    }
                }
                is AppResult.Error -> Unit // Non-fatal; conversation still usable
            }
        }
    }

    // Mesajlar tek doğru kaynaktan (Room) reaktif okunur. Global canlı senk yeni mesajı/durumu
    // Room'a yazdığı için bu Flow ekran açıkken kendiliğinden güncellenir (gönderen echo'su dahil).
    private fun observeMessages() {
        launch {
            observeRoomMessagesUseCase(roomId).collect { incoming ->
                messages = incoming
                snapshotUnreadAnchorIfNeeded()
                val items = buildListItems()
                val firstUnreadIndex = items
                    .indexOfFirst { it is ConversationListItem.UnreadDivider }
                    .takeIf { it >= 0 }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        items = items,
                        firstUnreadIndex = firstUnreadIndex
                    )
                }
                maybeMarkRead()
            }
        }
    }

    /**
     * Son gelen (karşı taraf) mesajın id'si üzerinden okundu bildirir. Boyut-karşılaştırma
     * sezgiseli yerine id tabanlı: eşit boyutlu güncellemeleri ve cache'ten açılışı kaçırmaz,
     * emisyon başına mükerrer PUT atmaz, başarısız PUT sonraki emisyonda kendiliğinden yeniden denenir.
     */
    private fun maybeMarkRead() {
        if (!isResumed || currentUserId == null || roomId <= 0) return
        val lastIncoming = messages.lastOrNull { !it.isMine() } ?: return
        if (lastIncoming.id == lastMarkReadRequestedId) return
        val requestedId = lastIncoming.id
        lastMarkReadRequestedId = requestedId
        launch {
            if (markMessagesAsReadUseCase(roomId) is AppResult.Error) {
                if (lastMarkReadRequestedId == requestedId) lastMarkReadRequestedId = null
            }
        }
    }

    private fun snapshotUnreadAnchorIfNeeded() {
        if (unreadAnchorCaptured || messages.isEmpty()) return
        unreadAnchorCaptured = true
        val firstUnread = messages.firstOrNull { !it.isMine() && !it.isRead }
        unreadAnchorMessageId = firstUnread?.id
        unreadAnchorCount = messages.count { !it.isMine() && !it.isRead }
    }

    // REST'ten geçmişi çekip Room'a yazar (ilk açılış + retry). Yerelde veri varken hata UI'yı bozmaz.
    private fun syncMessages() {
        if (messages.isEmpty()) _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        launch(onError = { e ->
            if (messages.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = UiText.Resource(R.string.common_error_unknown)) }
            }
            handleError(e)
        }) {
            when (val result = syncRoomMessagesUseCase(roomId)) {
                is AppResult.Success -> {
                    // Okundu bildirimi sync sonrası Room emisyonundaki maybeMarkRead ile atılır;
                    // burada ayrıca çağrılmaz (çifte PUT önlenir).
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                }
                is AppResult.Error -> _uiState.update {
                    if (messages.isEmpty()) it.copy(isLoading = false, errorMessage = result.error.toUiText()) else it
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
            // Hata: yazılan metni geri ver, gönderim durumunu sıfırla.
            _uiState.update { it.copy(isSending = false, inputText = text) }
        }) {
            // Taslak konuşma: önce sunucuda oda yarat, sonra mesajı gönder.
            if (roomId == 0L) {
                when (val result = createOrGetPrivateChatRoomUseCase(otherUserId)) {
                    is AppResult.Success -> {
                        roomId = result.data.id
                        // Yeni oda yayınını (echo) alabilmek için göndermeden ÖNCE odaya katıl.
                        joinChatRoomUseCase(roomId)
                        startRoomSession()
                    }
                    is AppResult.Error -> {
                        showError(result.error.toUiText())
                        _uiState.update { it.copy(isSending = false, inputText = text) }
                        return@launch
                    }
                }
            }
            // Sent via socket; the broadcast echo appends the message with its server id.
            when (val result = sendChatMessageUseCase(roomId, text)) {
                is AppResult.Success -> _uiState.update { it.copy(isSending = false) }
                is AppResult.Error -> {
                    // Soket bağlı değil/gönderilemedi: sessiz kalma — kullanıcıyı uyar, metni geri ver.
                    showError(result.error.toUiText())
                    _uiState.update { it.copy(isSending = false, inputText = text) }
                }
            }
        }
    }

    private fun respondToRequest(accept: Boolean) {
        if (_uiState.value.isRespondingRequest) return
        _uiState.update { it.copy(isRespondingRequest = true) }
        launch(onError = { e ->
            handleError(e)
            _uiState.update { it.copy(isRespondingRequest = false) }
        }) {
            val result = if (accept) acceptChatRequestUseCase(roomId) else declineChatRequestUseCase(roomId)
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isRespondingRequest = false, isIncomingRequest = false) }
                    if (!accept) _uiEffect.emit(ConversationUiEffect.NavigateBack)
                }
                is AppResult.Error -> _uiState.update {
                    it.copy(isRespondingRequest = false, errorMessage = result.error.toUiText())
                }
            }
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

    private fun buildListItems(): List<ConversationListItem> {
        // Sıralama DAO sözleşmesinden gelir (ChatMessageDao.observeByRoom: ORDER BY sent_at ASC);
        // burada yeniden sıralanmaz — her soket emisyonunda gereksiz O(n log n) biner.
        val result = mutableListOf<ConversationListItem>()
        var lastDate = ""
        messages.forEach { message ->
            val date = message.sentAt?.iso8601?.take(10) ?: ""
            if (date.isNotEmpty() && date != lastDate) {
                // Raw yyyy-MM-dd; the UI (rememberRelativeDateLabel) resolves the localized,
                // view-time-relative label (Today / Yesterday / weekday / date).
                result.add(ConversationListItem.DateHeader(date))
                lastDate = date
            }
            if (message.id == unreadAnchorMessageId) {
                result.add(ConversationListItem.UnreadDivider(unreadAnchorCount))
            }
            result.add(ConversationListItem.Message(message.toUiModel()))
        }
        return result
    }

    private fun ChatMessage.isMine(): Boolean =
        (currentUserId != null && senderUserId == currentUserId) || senderUsername == currentUsername

    private fun ChatMessage.toUiModel() = ChatMessageUiModel(
        id = id,
        content = content,
        time = sentAt?.iso8601?.substringAfter("T")?.take(5) ?: "",
        isMine = isMine(),
        status = status
    )
}
