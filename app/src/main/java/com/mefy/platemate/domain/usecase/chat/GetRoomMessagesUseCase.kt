package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class GetRoomMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(roomId: Long) = repository.getRoomMessages(roomId)
}
