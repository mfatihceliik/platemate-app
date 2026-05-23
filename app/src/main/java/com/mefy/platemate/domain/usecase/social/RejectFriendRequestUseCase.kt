package com.mefy.platemate.domain.usecase.social

import com.mefy.platemate.domain.repository.SocialRepository
import javax.inject.Inject

class RejectFriendRequestUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(id: Long) = repository.rejectFriendRequest(id)
}
