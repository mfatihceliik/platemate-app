package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String) =
        repository.register(username.trim(), email.trim(), password)
}
