package com.mefy.platemate.presentation.features.main.messages.chatdetail

sealed interface ChatDetailUiEffect {
    data object NavigateBack : ChatDetailUiEffect
    data class NavigateToUserProfile(val userId: Long) : ChatDetailUiEffect
    /** Deleting the conversation should land the user back on the Messages list, not the thread just deleted. */
    data object NavigateToMessagesList : ChatDetailUiEffect
}
