package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PlateAdminDto
import com.mefy.platemate.domain.model.admin.HiddenPlate
import javax.inject.Inject

class HiddenPlateMapper @Inject constructor() : Mapper<PlateAdminDto, HiddenPlate> {
    override fun map(input: PlateAdminDto): HiddenPlate = HiddenPlate(
        id = input.id,
        plateCode = input.plateCode.orEmpty(),
        statusCode = input.statusCode.orEmpty(),
        hiddenReason = input.hiddenReason.orEmpty(),
        reviewCount = input.reviewCount ?: 0,
        reportCount = input.reportCount ?: 0,
        updatedAt = input.updatedAt
    )
}