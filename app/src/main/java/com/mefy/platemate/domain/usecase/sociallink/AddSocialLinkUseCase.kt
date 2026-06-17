package com.mefy.platemate.domain.usecase.sociallink

import com.mefy.platemate.domain.repository.SocialLinkRepository
import javax.inject.Inject

class AddSocialLinkUseCase @Inject constructor(
    private val repository: SocialLinkRepository
) {
    suspend operator fun invoke(platform: String, url: String) =
        repository.addSocialLink(platform = platform, url = url)
}
