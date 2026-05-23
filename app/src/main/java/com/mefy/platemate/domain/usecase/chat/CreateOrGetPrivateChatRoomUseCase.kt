package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class CreateOrGetPrivateChatRoomUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(otherUserId: Long) = repository.createOrGetPrivateChatRoom(otherUserId)
}
