package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.model.admin.CommentReportReasonInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SaveCommentReportReasonUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(id: Long?, input: CommentReportReasonInput) =
        if (id == null) repository.addCommentReportReason(input) else repository.updateCommentReportReason(id, input)
}
