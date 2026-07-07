package com.mefy.platemate.domain.usecase.report

import com.mefy.platemate.domain.repository.UserReportRepository
import javax.inject.Inject

class ReportUserUseCase @Inject constructor(
    private val repository: UserReportRepository
) {
    suspend operator fun invoke(userId: Long, reason: String) = repository.reportUser(userId, reason)
}
