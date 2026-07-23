package com.mefy.platemate.presentation.features.main.messages.conversation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mefy.platemate.domain.model.chat.MessageStatus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.features.uimodel.ReportReason

sealed interface ConversationListItem {
    /** Day boundary as a stable `yyyy-MM-dd`; the UI resolves it to a localized label. */
    @Immutable
    data class DateHeader(val isoDate: String) : ConversationListItem

    @Immutable
    data class Message(val model: ChatMessageUiModel) : ConversationListItem

    /** WhatsApp tarzı "Okunmamış mesajlar" ayracı; oda açıldığı anki okunmamış sayısını taşır. */
    @Immutable
    data class UnreadDivider(val count: Int) : ConversationListItem

    /** Sunucudan gelen iş kuralı hatası (limit/engel/kapalı/red) — Room'a yazılmaz, oturum-içi. */
    @Immutable
    data class SystemInfo(val id: Long, val text: UiText) : ConversationListItem
}

@Immutable
data class ChatMessageUiModel(
    val id: Long,
    val content: String,
    val time: String,
    val isMine: Boolean,
    val status: MessageStatus,
    // Bu mesaj bir alıntıysa (Quote) dolu; null ise normal mesaj.
    val replyPreview: ReplyPreviewUiModel? = null
)

/** Bir balonun içinde gösterilen küçük alıntı kartı — alıntılanan mesajın önizlemesi. */
@Immutable
data class ReplyPreviewUiModel(
    val messageId: Long,
    val senderLabel: UiText,
    val contentPreview: String
)

@Immutable
data class ConversationUiState(
    val isLoading: Boolean = true,
    val errorMessage: UiText? = null,
    val participantName: String = "",
    val items: List<ConversationListItem> = emptyList(),
    // items içindeki UnreadDivider'ın index'i; ilk açılışta liste buraya kaydırılır.
    val firstUnreadIndex: Int? = null,
    // Mesaj aramasından "gitme" ile açıldıysa hedef mesajın items içindeki index'i; doluysa
    // firstUnreadIndex'ten önceliklidir (bkz. ConversationScreen'in ilk-scroll efekti).
    val scrollToMessageIndex: Int? = null,
    val inputText: String = "",
    // Other participant's online status; null = unknown (no data yet / not visible).
    val isOtherUserOnline: Boolean? = null,
    // Incoming message request awaiting this user's approval
    val isIncomingRequest: Boolean = false,
    val isRespondingRequest: Boolean = false,
    // Non-null while the WhatsApp-tarzı uzun-basma aksiyon menüsü (Alıntıla/Raporla/Sil) açık.
    val actionMenuTargetId: Long? = null,
    // Non-null while the delete-confirm bottom sheet (menüdeki "Sil"den sonraki ikinci adım) açık.
    val deleteConfirmTargetId: Long? = null,
    // Composer'ın üzerinde gösterilen alıntı önizlemesi; "Alıntıla" ile dolar, X ile veya
    // gönderim sonrası temizlenir.
    val replyTarget: ReplyPreviewUiModel? = null,
    // Non-null iken rapor bottom sheet'i açık — hangi mesaj için raporlandığını da taşır.
    val reportTarget: ReplyPreviewUiModel? = null,
    val selectedReportReason: ReportReason? = null,
    val otherReportReasonText: String = "",
    val isSubmittingReport: Boolean = false
)
