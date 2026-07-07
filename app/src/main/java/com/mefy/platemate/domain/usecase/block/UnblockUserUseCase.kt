package com.mefy.platemate.domain.usecase.block

import com.mefy.platemate.domain.repository.UserBlockRepository
import javax.inject.Inject

class UnblockUserUseCase @Inject constructor(
    private val repository: UserBlockRepository
) {
    suspend operator fun invoke(userId: Long) = repository.unblockUser(userId)
}
