package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class ReviewCommentReportUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(reportId: Long, statusCode: String, adminNote: String?) =
        repository.reviewCommentReport(reportId, statusCode, adminNote)
}
