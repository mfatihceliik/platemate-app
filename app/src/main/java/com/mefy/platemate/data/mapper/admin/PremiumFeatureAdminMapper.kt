package com.mefy.platemate.data.mapper.admin

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.admin.PremiumFeatureAdminDto
import com.mefy.platemate.domain.model.admin.PremiumFeatureAdmin
import javax.inject.Inject
import kotlin.collections.orEmpty

class PremiumFeatureAdminMapper @Inject constructor() : Mapper<PremiumFeatureAdminDto, PremiumFeatureAdmin> {
    override fun map(input: PremiumFeatureAdminDto): PremiumFeatureAdmin = PremiumFeatureAdmin(
        id = input.id,
        iconKey = input.iconKey.orEmpty(),
        titles = input.titles.orEmpty(),
        subtitles = input.subtitles,
        sortOrder = input.sortOrder ?: 0,
        active = input.active
    )
}