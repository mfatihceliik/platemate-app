package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class GetPendingCommentsUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(page: Int, size: Int = DEFAULT_PAGE_SIZE) =
        repository.getPendingComments(page, size)

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
