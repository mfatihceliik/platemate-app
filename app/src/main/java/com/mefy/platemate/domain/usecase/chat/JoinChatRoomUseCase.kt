package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class JoinChatRoomUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(roomId: Long) = repository.joinRoom(roomId)
}
