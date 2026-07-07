package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class RemoveCommentUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(commentId: Long, reason: String?) =
        repository.removeComment(commentId, reason)
}
