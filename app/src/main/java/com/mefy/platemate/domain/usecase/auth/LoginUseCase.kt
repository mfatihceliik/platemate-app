package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        repository.login(email.trim(), password)
}
