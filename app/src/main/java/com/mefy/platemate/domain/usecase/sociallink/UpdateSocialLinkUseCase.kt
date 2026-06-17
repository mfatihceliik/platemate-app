package com.mefy.platemate.domain.usecase.sociallink

import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.repository.SocialLinkRepository
import javax.inject.Inject

class UpdateSocialLinkUseCase @Inject constructor(
    private val repository: SocialLinkRepository
) {
    suspend operator fun invoke(link: SocialMediaLink) = repository.updateSocialLink(link)
}
