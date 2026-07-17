package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalReasonAdminDto
import com.mefy.platemate.domain.model.admin.PlateRemovalReasonAdmin
import javax.inject.Inject

class PlateRemovalReasonAdminMapper @Inject constructor() : Mapper<PlateRemovalReasonAdminDto, PlateRemovalReasonAdmin> {
    override fun map(input: PlateRemovalReasonAdminDto): PlateRemovalReasonAdmin {
        return PlateRemovalReasonAdmin(
            id = input.id,
            code = input.code,
            label = input.label,
            requiresDescription = input.requiresDescription,
            sortOrder = input.sortOrder,
            active = input.active,
            createdAt = input.createdAt,
            updatedAt = input.updatedAt
        )
    }
}
