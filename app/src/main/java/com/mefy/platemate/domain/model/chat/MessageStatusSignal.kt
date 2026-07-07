package com.mefy.platemate.domain.model.chat

/**
 * Real-time delivery/read update for a sender. messageId is null for room-wide
 * updates (e.g. all messages in a room marked read).
 */
data class MessageStatusSignal(
    val roomId: Long,
    val messageId: Long?,
    val status: MessageStatus
)
