package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class SyncChatRoomsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() = repository.syncChatRooms()
}
