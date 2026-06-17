package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.data.remote.dto.social.SocialMediaLinkDto
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import javax.inject.Inject

class SocialMediaLinkMapper @Inject constructor() : Mapper<SocialMediaLinkDto, SocialMediaLink> {
    override fun map(input: SocialMediaLinkDto): SocialMediaLink = SocialMediaLink(
        id = input.id,
        platform = input.platformCode ?: "UNKNOWN",
        url = input.url
    )
}
