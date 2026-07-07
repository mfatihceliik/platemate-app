package com.mefy.platemate.presentation.features.main.messages.conversation

sealed interface ConversationUiAction {
    data class InputChanged(val text: String) : ConversationUiAction
    data object SendClicked : ConversationUiAction
    data object InfoClicked : ConversationUiAction
    data object BackClicked : ConversationUiAction
    data object RetryClicked : ConversationUiAction
    data object AcceptRequestClicked : ConversationUiAction
    data object DeclineRequestClicked : ConversationUiAction

    // Yaşam döngüsü: "okundu" yalnızca ekran gerçekten görünürken (RESUMED) gönderilir;
    // uygulama arka plandayken sokete mesaj düşerse karşıya yanlış "görüldü" gitmez.
    data object ScreenResumed : ConversationUiAction
    data object ScreenPaused : ConversationUiAction
}
