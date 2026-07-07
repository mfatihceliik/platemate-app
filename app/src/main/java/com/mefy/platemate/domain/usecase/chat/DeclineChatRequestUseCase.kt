package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class DeclineChatRequestUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(roomId: Long) = repository.declineChatRequest(roomId)
}
