package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.repository.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String) =
        repository.changePassword(currentPassword, newPassword)
}
