package com.mefy.platemate.presentation.features.main.messages.chatdetail

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ChatDetailUiState(
    val conversationId: String = "",
    val participantName: String = "",
    val initials: String = "",
    val avatarBg: Color = Color(0xFFEEF2FF),
    val avatarFg: Color = Color(0xFF4F46E5),
    val notificationsEnabled: Boolean = true,
    val otherUserId: Long? = null,
    val isBlocked: Boolean = false,
    val isBlockInProgress: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isDeleting: Boolean = false
)
