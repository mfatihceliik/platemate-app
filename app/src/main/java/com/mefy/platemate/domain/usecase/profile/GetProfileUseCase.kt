package com.mefy.platemate.domain.usecase.profile

import com.mefy.platemate.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: Long) = repository.getProfile(userId)
}
