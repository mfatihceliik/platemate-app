package com.mefy.platemate.presentation.features.main.profile.userprofile

sealed interface UserProfileUiEffect {
    data object NavigateBack : UserProfileUiEffect
    data class NavigateToChat(
        val conversationId: String,
        val otherUserId: Long,
        val participantName: String,
    ) : UserProfileUiEffect
}
