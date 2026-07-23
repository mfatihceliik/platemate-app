package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
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
import com.mefy.platemate.domain.usecase.chat.DeleteChatMessageUseCase
import com.mefy.platemate.domain.usecase.chat.GetChatRoomUseCase
import com.mefy.platemate.domain.usecase.chat.GetRoomPresenceUseCase
import com.mefy.platemate.domain.usecase.chat.JoinChatRoomUseCase
import com.mefy.platemate.domain.usecase.chat.MarkMessagesAsReadUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveChatErrorsUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveChatRoomsUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveUserPresenceUseCase
import com.mefy.platemate.domain.usecase.chat.ObserveRoomMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.PurgeDeletedMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.ReportMessageUseCase
import com.mefy.platemate.domain.usecase.chat.RetryFailedMessageUseCase
import com.mefy.platemate.domain.usecase.chat.SyncChatRoomsUseCase
import com.mefy.platemate.domain.usecase.chat.SyncRoomMessagesUseCase
import com.mefy.platemate.domain.usecase.chat.SendChatMessageUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import com.mefy.platemate.presentation.features.uimodel.ReportReason
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
    private val retryFailedMessageUseCase: RetryFailedMessageUseCase,
    private val deleteChatMessageUseCase: DeleteChatMessageUseCase,
    private val reportMessageUseCase: ReportMessageUseCase,
    private val purgeDeletedMessagesUseCase: PurgeDeletedMessagesUseCase,
    private val joinChatRoomUseCase: JoinChatRoomUseCase,
    private val observeChatErrorsUseCase: ObserveChatErrorsUseCase,
    private val getRoomPresenceUseCase: GetRoomPresenceUseCase,
    private val observeUserPresenceUseCase: ObserveUserPresenceUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
    private val acceptChatRequestUseCase: AcceptChatRequestUseCase,
    private val declineChatRequestUseCase: DeclineChatRequestUseCase,
    private val createOrGetPrivateChatRoomUseCase: CreateOrGetPrivateChatRoomUseCase,
    private val observeChatRoomsUseCase: ObserveChatRoomsUseCase,
    private val syncChatRoomsUseCase: SyncChatRoomsUseCase,
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

    // Sunucudan gelen iş kuralı hataları (limit/engel/kapalı/red) — Room'a yazılmaz, sadece bu
    // ekran oturumu boyunca thread'in sonunda kalıcı gösterilir (bkz. observeErrors/buildListItems).
    private val systemInfoMessages = mutableListOf<UiText>()

    init {
        observeErrors()
        launch {
            val session = observeSessionUseCase().first()
            currentUserId = session?.userId
            currentUsername = session?.username ?: ""
            if (roomId > 0) {
                startRoomSession()
            } else if (otherUserId > 0) {
                resolveExistingRoomOrShowDraft()
            } else {
                // Taslak konuşma: oda yok, yükleme yok; ilk mesajla oluşacak.
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * conversationId boş ama otherUserId dolu geldiğinde (ör. UserProfileScreen'in mesaj
     * butonu) önce yerelde (Room DB, ağ çağrısı yok) bu kullanıcıyla mevcut bir oda var mı
     * bakılır; yoksa bir kez senkronize edilip tekrar bakılır. Bulunursa geçmiş hemen yüklenir —
     * artık sadece ilk mesaj gönderilince değil. createOrGetPrivateChatRoomUseCase burada
     * ÇAĞRILMAZ: o oda yaratır, sırf ekranı açarak (mesaj göndermeden) boş oda oluşturmamak için
     * taslak davranışı (ilk mesajla oluşturma) korunur.
     */
    private suspend fun resolveExistingRoomOrShowDraft() {
        var existing = observeChatRoomsUseCase().first()
            .firstOrNull { it.otherParticipantId == otherUserId }
        if (existing == null) {
            syncChatRoomsUseCase()
            existing = observeChatRoomsUseCase().first()
                .firstOrNull { it.otherParticipantId == otherUserId }
        }
        if (existing != null) {
            roomId = existing.id
            joinChatRoomUseCase(roomId)
            startRoomSession()
        } else {
            _uiState.update { it.copy(isLoading = false) }
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
        launch {
            // Önceki oturumdan kalan taşlaşmış (DELETED) satırlar temizlenir, SONRA gözlem/senkron
            // başlar — sıralama bozulursa Flow ilk anlık görüntüde eski "silindi" balonunu bir kare
            // gösterip kaybedebilir. Canlı oturumda (reconnect resync vb.) tekrar çağrılmaz — bu
            // yüzden az önce silinen bir mesajın balonu ekrandayken kaybolmaz.
            purgeDeletedMessagesUseCase(roomId)
            observeMessages()
            syncMessages()
        }
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
                showError(UiText.Dynamic(message))
                // Snackbar geçici/kaçırılabilir; aynı mesajı thread içinde kalıcı da göster.
                systemInfoMessages.add(UiText.Dynamic(message))
                _uiState.update { it.copy(items = buildListItems()) }
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
            is ConversationUiAction.RetryMessageClicked -> retryMessage(action.messageId)
            is ConversationUiAction.MessageLongPressed -> onMessageLongPressed(action.messageId)
            ConversationUiAction.ActionMenuDismissed ->
                _uiState.update { it.copy(actionMenuTargetId = null) }
            is ConversationUiAction.QuoteMessageClicked -> onQuoteMessageClicked(action.messageId)
            ConversationUiAction.CancelReplyClicked ->
                _uiState.update { it.copy(replyTarget = null) }
            is ConversationUiAction.ReportMessageClicked -> onReportMessageClicked(action.messageId)
            is ConversationUiAction.ReportReasonSelected ->
                _uiState.update { it.copy(selectedReportReason = action.reason) }
            is ConversationUiAction.ReportOtherReasonTextChanged ->
                _uiState.update { it.copy(otherReportReasonText = action.text) }
            ConversationUiAction.ReportSubmitClicked -> submitReport()
            ConversationUiAction.ReportDismissed ->
                _uiState.update {
                    it.copy(reportTarget = null, selectedReportReason = null, otherReportReasonText = "")
                }
            is ConversationUiAction.DeleteActionClicked ->
                _uiState.update {
                    it.copy(actionMenuTargetId = null, deleteConfirmTargetId = action.messageId)
                }
            is ConversationUiAction.DeleteMessageConfirmed -> deleteMessage(action.messageId)
            ConversationUiAction.DeleteConfirmDismissed ->
                _uiState.update { it.copy(deleteConfirmTargetId = null) }
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
                    _uiState.update {
                        it.copy(
                            isIncomingRequest = incoming,
                            participantName = displayName,
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
                val scrollToMessageIndex = route.targetMessageId?.let { targetId ->
                    items
                        .indexOfFirst { it is ConversationListItem.Message && it.model.id == targetId }
                        .takeIf { it >= 0 }
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        items = items,
                        firstUnreadIndex = firstUnreadIndex,
                        scrollToMessageIndex = scrollToMessageIndex
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

    // Per-message send state (PENDING/SENT/FAILED) now lives entirely in ChatMessageUiModel.status,
    // reactive via Room → observeRoomMessages() → buildListItems() — there is no screen-level
    // "isSending" flag anymore, since rapid-fire sends can have several messages in flight at
    // once and a single boolean can't represent that.
    private fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isBlank()) return
        val replyTarget = _uiState.value.replyTarget

        _uiState.update { it.copy(inputText = "", replyTarget = null) }
        launch(onError = { e ->
            handleError(e)
            _uiState.update { it.copy(inputText = text, replyTarget = replyTarget) }
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
                        _uiState.update { it.copy(inputText = text, replyTarget = replyTarget) }
                        return@launch
                    }
                }
            }
            // Repository writes an optimistic PENDING row to Room before attempting the socket
            // send, so the message is already visible here regardless of outcome; this call only
            // fails (AppResult.Error) in the rare case there's no active session to send as.
            when (val result = sendChatMessageUseCase(roomId, text, replyTarget?.messageId)) {
                is AppResult.Success -> Unit
                is AppResult.Error -> {
                    showError(result.error.toUiText())
                    _uiState.update { it.copy(inputText = text, replyTarget = replyTarget) }
                }
            }
        }
    }

    private fun retryMessage(messageId: Long) {
        launch(onError = { handleError(it) }) {
            retryFailedMessageUseCase(roomId, messageId)
        }
    }

    // Herhangi bir mesaj için aksiyon menüsünü açar (Alıntıla/Raporla/Sil); Sil sadece kendi
    // mesajların için, Raporla sadece karşı tarafın mesajları için satır olarak gösterilir —
    // gating render tarafında (MessageActionMenu), burada değil.
    private fun onMessageLongPressed(messageId: Long) {
        _uiState.update { it.copy(actionMenuTargetId = messageId) }
    }

    private fun onQuoteMessageClicked(messageId: Long) {
        val message = messages.firstOrNull { it.id == messageId } ?: run {
            _uiState.update { it.copy(actionMenuTargetId = null) }
            return
        }
        val senderLabel = if (message.isMine()) {
            UiText.Resource(R.string.common_you)
        } else {
            UiText.Dynamic(message.senderUsername)
        }
        _uiState.update {
            it.copy(
                actionMenuTargetId = null,
                replyTarget = ReplyPreviewUiModel(
                    messageId = message.id,
                    senderLabel = senderLabel,
                    contentPreview = message.content
                )
            )
        }
    }

    private fun onReportMessageClicked(messageId: Long) {
        val message = messages.firstOrNull { it.id == messageId } ?: run {
            _uiState.update { it.copy(actionMenuTargetId = null) }
            return
        }
        _uiState.update {
            it.copy(
                actionMenuTargetId = null,
                reportTarget = ReplyPreviewUiModel(
                    messageId = message.id,
                    senderLabel = UiText.Dynamic(message.senderUsername),
                    contentPreview = message.content
                )
            )
        }
    }

    private fun submitReport() {
        val target = _uiState.value.reportTarget ?: return
        val reason = _uiState.value.selectedReportReason ?: return
        val otherText = _uiState.value.otherReportReasonText
        val reasonString = if (reason == ReportReason.OTHER && otherText.isNotBlank()) {
            "${reason.name}: $otherText"
        } else {
            reason.name
        }

        _uiState.update { it.copy(isSubmittingReport = true) }
        launch(onError = { e ->
            _uiState.update { it.copy(isSubmittingReport = false) }
            handleError(e)
        }) {
            val result = reportMessageUseCase(target.messageId, reasonString)
            _uiState.update {
                it.copy(
                    isSubmittingReport = false,
                    reportTarget = null,
                    selectedReportReason = null,
                    otherReportReasonText = ""
                )
            }
            when (result) {
                is AppResult.Success -> showSuccess(UiText.Resource(R.string.user_profile_report_submitted))
                is AppResult.Error -> showError(result.error.toUiText())
            }
        }
    }

    private fun deleteMessage(messageId: Long) {
        _uiState.update { it.copy(deleteConfirmTargetId = null) }
        launch(onError = { handleError(it) }) {
            when (val result = deleteChatMessageUseCase(messageId)) {
                is AppResult.Success -> Unit
                is AppResult.Error -> showError(result.error.toUiText())
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
        systemInfoMessages.forEachIndexed { index, text ->
            result.add(ConversationListItem.SystemInfo(id = index.toLong(), text = text))
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
        status = status,
        replyPreview = replyToMessageId?.let { targetId ->
            ReplyPreviewUiModel(
                messageId = targetId,
                senderLabel = if (replyToSenderUsername == currentUsername) {
                    UiText.Resource(R.string.common_you)
                } else {
                    UiText.Dynamic(replyToSenderUsername.orEmpty())
                },
                contentPreview = replyToContentPreview.orEmpty()
            )
        }
    )
}
