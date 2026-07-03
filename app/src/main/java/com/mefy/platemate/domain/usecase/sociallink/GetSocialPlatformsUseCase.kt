package com.mefy.platemate.domain.usecase.sociallink

import com.mefy.platemate.domain.repository.SocialLinkRepository
import javax.inject.Inject

class GetSocialPlatformsUseCase @Inject constructor(
    private val repository: SocialLinkRepository
) {
    suspend operator fun invoke() = repository.getSocialPlatforms()
}
