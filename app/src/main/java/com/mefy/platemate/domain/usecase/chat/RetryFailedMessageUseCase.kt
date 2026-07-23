package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class RetryFailedMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatRoomId: Long, messageId: Long) =
        repository.retryMessage(chatRoomId, messageId)
}
