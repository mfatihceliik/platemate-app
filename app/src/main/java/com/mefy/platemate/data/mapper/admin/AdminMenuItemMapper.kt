package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.AdminMenuItemDto
import com.mefy.platemate.domain.model.admin.AdminMenuItem
import javax.inject.Inject
import kotlin.text.orEmpty

class AdminMenuItemMapper @Inject constructor() : Mapper<AdminMenuItemDto, AdminMenuItem> {
    override fun map(input: AdminMenuItemDto): AdminMenuItem = AdminMenuItem(
        code = input.code.orEmpty(),
        title = input.title.orEmpty(),
        iconKey = input.iconKey.orEmpty(),
        sortOrder = input.sortOrder ?: 0,
        badgeCount = input.badgeCount
    )
}