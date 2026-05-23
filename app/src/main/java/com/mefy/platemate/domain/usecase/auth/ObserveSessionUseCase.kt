package com.mefy.platemate.domain.usecase.auth

import com.mefy.platemate.domain.repository.AuthRepository
import javax.inject.Inject

class ObserveSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.session
}
