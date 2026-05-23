package com.mefy.platemate.domain.usecase.social

import com.mefy.platemate.domain.repository.SocialRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val repository: SocialRepository
) {
    suspend operator fun invoke() = repository.getFriends()
}
