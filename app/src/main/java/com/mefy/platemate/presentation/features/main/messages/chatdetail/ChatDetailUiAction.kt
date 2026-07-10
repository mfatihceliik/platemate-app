package com.mefy.platemate.presentation.features.main.messages.chatdetail

sealed interface ChatDetailUiAction {
    data object BackClicked : ChatDetailUiAction
    data object MessageClicked : ChatDetailUiAction
    data object ProfileClicked : ChatDetailUiAction
    data object NotificationsToggled : ChatDetailUiAction
    data object DeleteChatClicked : ChatDetailUiAction
    data object ReportClicked : ChatDetailUiAction
    data object BlockClicked : ChatDetailUiAction
}
