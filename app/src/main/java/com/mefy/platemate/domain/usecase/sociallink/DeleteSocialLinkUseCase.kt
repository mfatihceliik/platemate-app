package com.mefy.platemate.domain.usecase.sociallink

import com.mefy.platemate.domain.repository.SocialLinkRepository
import javax.inject.Inject

class DeleteSocialLinkUseCase @Inject constructor(
    private val repository: SocialLinkRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteSocialLink(id)
}
