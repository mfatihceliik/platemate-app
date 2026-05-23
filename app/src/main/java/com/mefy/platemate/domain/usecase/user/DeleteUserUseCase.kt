package com.mefy.platemate.domain.usecase.user

import com.mefy.platemate.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteUser(id)
}
