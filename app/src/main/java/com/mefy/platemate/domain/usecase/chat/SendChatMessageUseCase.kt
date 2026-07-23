package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatRoomId: Long, content: String, replyToMessageId: Long? = null) =
        repository.sendMessage(chatRoomId, content.trim(), replyToMessageId)
}
