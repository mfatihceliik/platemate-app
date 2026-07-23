package com.mefy.platemate.presentation.features.main.messages.conversation

import com.mefy.platemate.presentation.features.uimodel.ReportReason

sealed interface ConversationUiAction {
    data class InputChanged(val text: String) : ConversationUiAction
    data object SendClicked : ConversationUiAction
    data object InfoClicked : ConversationUiAction
    data object BackClicked : ConversationUiAction
    // Tap on a FAILED message's red-exclamation icon: resend that specific message.
    data class RetryMessageClicked(val messageId: Long) : ConversationUiAction

    // Long-press on a bubble: opens WhatsApp-tarzı aksiyon menüsü (Alıntıla/Raporla/Sil) — herhangi
    // bir mesaj için, sadece kendi mesajların için değil.
    data class MessageLongPressed(val messageId: Long) : ConversationUiAction
    data object ActionMenuDismissed : ConversationUiAction
    data class QuoteMessageClicked(val messageId: Long) : ConversationUiAction
    // Menüdeki "Raporla" satırı: aksiyon menüsünü kapatır, rapor bottom sheet'ini açar.
    data class ReportMessageClicked(val messageId: Long) : ConversationUiAction
    data class ReportReasonSelected(val reason: ReportReason) : ConversationUiAction
    data class ReportOtherReasonTextChanged(val text: String) : ConversationUiAction
    data object ReportSubmitClicked : ConversationUiAction
    data object ReportDismissed : ConversationUiAction

    // Composer'ın üzerindeki alıntı önizlemesinin X butonu.
    data object CancelReplyClicked : ConversationUiAction

    // Menüdeki "Sil" satırı: aksiyon menüsünü kapatır, delete-confirm sheet'i açar.
    data class DeleteActionClicked(val messageId: Long) : ConversationUiAction
    data class DeleteMessageConfirmed(val messageId: Long) : ConversationUiAction
    data object DeleteConfirmDismissed : ConversationUiAction
    data object AcceptRequestClicked : ConversationUiAction
    data object DeclineRequestClicked : ConversationUiAction

    // Yaşam döngüsü: "okundu" yalnızca ekran gerçekten görünürken (RESUMED) gönderilir;
    // uygulama arka plandayken sokete mesaj düşerse karşıya yanlış "görüldü" gitmez.
    data object ScreenResumed : ConversationUiAction
    data object ScreenPaused : ConversationUiAction
}
