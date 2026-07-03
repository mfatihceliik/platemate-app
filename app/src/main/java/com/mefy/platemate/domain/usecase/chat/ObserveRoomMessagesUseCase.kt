package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class ObserveRoomMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(roomId: Long) = repository.observeRoomMessages(roomId)
}
