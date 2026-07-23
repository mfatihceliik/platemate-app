package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.AccentColorAdminDto
import com.mefy.platemate.domain.model.admin.AccentColorAdmin
import javax.inject.Inject

class AccentColorAdminMapper @Inject constructor() : Mapper<AccentColorAdminDto, AccentColorAdmin> {
    override fun map(input: AccentColorAdminDto): AccentColorAdmin = AccentColorAdmin(
        id = input.id,
        hex = input.hex.orEmpty(),
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}