package com.mefy.platemate.domain.usecase.social

import com.mefy.platemate.domain.repository.SocialRepository
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke(addresseeId: Long) = repository.sendFriendRequest(addresseeId)
}
