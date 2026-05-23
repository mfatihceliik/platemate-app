package com.mefy.platemate.domain.usecase.user

import com.mefy.platemate.domain.repository.UserRepository
import javax.inject.Inject

class UpdateCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String?, password: String?) =
        repository.updateCurrentUser(email?.trim(), password)
}
