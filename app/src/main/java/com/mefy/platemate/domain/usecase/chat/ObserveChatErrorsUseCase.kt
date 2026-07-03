package com.mefy.platemate.domain.usecase.chat

import com.mefy.platemate.domain.repository.ChatRepository
import javax.inject.Inject

class ObserveChatErrorsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() = repository.observeChatErrors()
}
