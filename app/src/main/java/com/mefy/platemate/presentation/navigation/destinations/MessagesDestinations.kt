package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MessagesDestination : AppDestination

@Serializable
data class ChatDestination(
    val conversationId: String,
    val otherUserId: Long = 0L,
    val participantName: String = "",
    val initials: String = "",
    val avatarBgArgb: Long = 0xFFEEF2FFL,
    val avatarFgArgb: Long = 0xFF4F46E5L
) : AppDestination

@Serializable
data class ChatDetailDestination(
    val conversationId: String,
    val participantName: String,
    val initials: String,
    val avatarBgArgb: Long,
    val avatarFgArgb: Long
) : AppDestination
