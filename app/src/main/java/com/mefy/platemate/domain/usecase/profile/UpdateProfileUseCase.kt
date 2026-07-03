package com.mefy.platemate.domain.usecase.profile

import com.mefy.platemate.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        userId: Long,
        displayName: String?,
        username: String?,
        bio: String?,
        profilePhotoUrl: String?
    ) = repository.updateProfile(userId, displayName, username, bio, profilePhotoUrl)
}
