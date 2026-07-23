package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PlateReportTypeAdminDto
import com.mefy.platemate.domain.model.admin.ReportTypeAdmin
import javax.inject.Inject
import kotlin.text.orEmpty

class ReportTypeAdminMapper @Inject constructor() : Mapper<PlateReportTypeAdminDto, ReportTypeAdmin> {
    override fun map(input: PlateReportTypeAdminDto): ReportTypeAdmin = ReportTypeAdmin(
        id = input.id,
        code = input.code.orEmpty(),
        label = input.label.orEmpty(),
        description = input.description.orEmpty(),
        iconKey = input.iconKey.orEmpty(),
        severityCode = input.severityCode.orEmpty(),
        colorHex = input.colorHex.orEmpty(),
        weight = input.weight ?: 0,
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}