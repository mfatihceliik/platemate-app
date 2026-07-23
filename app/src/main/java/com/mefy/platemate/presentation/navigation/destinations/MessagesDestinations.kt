package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MessagesDestination : AppDestination

@Serializable
data class ChatDestination(
    val conversationId: String,
    val otherUserId: Long = 0L,
    val participantName: String = "",
    val targetMessageId: Long? = null
) : AppDestination

@Serializable
data class ChatDetailDestination(
    val conversationId: String,
    val participantName: String
) : AppDestination
