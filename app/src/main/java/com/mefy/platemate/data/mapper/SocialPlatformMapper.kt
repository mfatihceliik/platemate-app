package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.social.SocialPlatformDto
import com.mefy.platemate.domain.model.social.SocialPlatform
import javax.inject.Inject

class SocialPlatformMapper @Inject constructor() : Mapper<SocialPlatformDto, SocialPlatform> {
    override fun map(input: SocialPlatformDto): SocialPlatform = SocialPlatform(
        id = input.id,
        code = input.code.orEmpty(),
        label = input.label.orEmpty(),
        iconUrl = input.iconUrl,
        backgroundColorHex = input.backgroundColorHex,
        iconTintColorHex = input.iconTintColorHex,
        sortOrder = input.sortOrder ?: 0
    )
}
