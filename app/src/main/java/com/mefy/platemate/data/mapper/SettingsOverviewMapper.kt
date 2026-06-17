package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.dto.settings.SettingsOverviewDto
import com.mefy.platemate.domain.model.settings.SettingsOverview
import javax.inject.Inject

class SettingsOverviewMapper @Inject constructor(
    private val userSettingsMapper: UserSettingsMapper,
    private val socialMediaLinkMapper: SocialMediaLinkMapper
) : Mapper<SettingsOverviewDto, SettingsOverview> {
    override fun map(input: SettingsOverviewDto): SettingsOverview = SettingsOverview(
        username = input.username,
        email = input.email,
        joinedAt = input.joinedAt,
        premiumActive = input.premiumActive,
        premiumUntil = input.premiumUntil,
        userSettings = userSettingsMapper.map(input.userSettings),
        socialMediaLinks = socialMediaLinkMapper.mapList(input.socialMediaLinks)
    )
}
