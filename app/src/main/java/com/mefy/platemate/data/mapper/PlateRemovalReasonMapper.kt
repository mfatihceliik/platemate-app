package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.plate.PlateRemovalReasonDto
import com.mefy.platemate.domain.model.plate.PlateRemovalReason
import javax.inject.Inject

class PlateRemovalReasonMapper @Inject constructor() : Mapper<PlateRemovalReasonDto, PlateRemovalReason> {
    override fun map(input: PlateRemovalReasonDto): PlateRemovalReason {
        return PlateRemovalReason(
            id = input.id,
            code = input.code,
            label = input.label,
            requiresDescription = input.requiresDescription
        )
    }
}
