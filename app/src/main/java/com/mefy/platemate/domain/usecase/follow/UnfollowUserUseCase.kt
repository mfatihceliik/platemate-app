package com.mefy.platemate.domain.usecase.follow

import com.mefy.platemate.domain.repository.FollowRepository
import javax.inject.Inject

class UnfollowUserUseCase @Inject constructor(
    private val repository: FollowRepository
) {
    suspend operator fun invoke(userId: Long) = repository.unfollowUser(userId)
}
