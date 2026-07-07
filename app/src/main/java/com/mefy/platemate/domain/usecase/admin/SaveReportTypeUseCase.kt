package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.model.admin.ReportTypeInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SaveReportTypeUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    /** id == null -> create, otherwise update. */
    suspend operator fun invoke(id: Long?, input: ReportTypeInput) =
        if (id == null) repository.addReportType(input) else repository.updateReportType(id, input)
}
