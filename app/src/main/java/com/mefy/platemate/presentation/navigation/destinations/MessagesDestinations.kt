package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MessagesDestination : AppDestination

@Serializable
data class ChatDestination(
    val conversationId: String
) : AppDestination
