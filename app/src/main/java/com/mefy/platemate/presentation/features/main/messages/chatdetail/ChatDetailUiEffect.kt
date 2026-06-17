package com.mefy.platemate.presentation.features.main.messages.chatdetail

sealed interface ChatDetailUiEffect {
    data object NavigateBack : ChatDetailUiEffect
}
