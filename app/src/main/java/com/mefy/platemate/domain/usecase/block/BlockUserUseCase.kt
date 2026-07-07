package com.mefy.platemate.domain.usecase.block

import com.mefy.platemate.domain.repository.UserBlockRepository
import javax.inject.Inject

class BlockUserUseCase @Inject constructor(
    private val repository: UserBlockRepository
) {
    suspend operator fun invoke(userId: Long) = repository.blockUser(userId)
}
