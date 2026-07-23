package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PlateRemovalRequestDto
import com.mefy.platemate.domain.model.admin.PlateRemovalRequest
import javax.inject.Inject

class PlateRemovalRequestMapper @Inject constructor() : Mapper<PlateRemovalRequestDto, PlateRemovalRequest> {
    override fun map(input: PlateRemovalRequestDto): PlateRemovalRequest = PlateRemovalRequest(
        id = input.id,
        plateCode = input.plateCode.orEmpty(),
        requesterUsername = input.requesterUsername.orEmpty(),
        requesterEmail = input.requesterEmail.orEmpty(),
        reasonCode = input.reasonCode.orEmpty(),
        description = input.description.orEmpty(),
        statusCode = input.statusCode.orEmpty(),
        createdAt = input.createdAt
    )
}