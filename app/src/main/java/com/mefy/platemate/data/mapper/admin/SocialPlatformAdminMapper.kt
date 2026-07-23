package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.SocialPlatformAdminDto
import com.mefy.platemate.domain.model.admin.SocialPlatformAdmin
import javax.inject.Inject
import kotlin.collections.orEmpty
import kotlin.text.orEmpty

class SocialPlatformAdminMapper @Inject constructor() : Mapper<SocialPlatformAdminDto, SocialPlatformAdmin> {
    override fun map(input: SocialPlatformAdminDto): SocialPlatformAdmin = SocialPlatformAdmin(
        id = input.id,
        code = input.code.orEmpty(),
        labels = input.labels.orEmpty(),
        iconUrl = input.iconUrl.orEmpty(),
        backgroundColorHex = input.backgroundColorHex.orEmpty(),
        iconTintColorHex = input.iconTintColorHex.orEmpty(),
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}