package com.mefy.platemate.domain.model.chat

/** Live "delete for everyone" push: the message stays, its content is cleared (tombstoned). */
data class MessageDeletedSignal(
    val roomId: Long,
    val messageId: Long
)
