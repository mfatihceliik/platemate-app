package com.mefy.platemate.domain.usecase.social

import com.mefy.platemate.domain.repository.SocialRepository
import javax.inject.Inject

class RemoveFriendUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(id: Long) = repository.removeFriend(id)
}
