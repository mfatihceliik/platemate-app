package com.mefy.platemate.domain.usecase.fcm

import com.mefy.platemate.domain.repository.FcmTokenRepository
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val repository: FcmTokenRepository
) {
    suspend operator fun invoke(token: String) = repository.registerToken(token)
}
