package com.mefy.platemate.presentation.features.main.profile.mapper

import com.mefy.platemate.domain.model.profile.UserProfile
import com.mefy.platemate.domain.model.social.SocialPlatform

interface ProfileUiMapper {
    fun mapProfile(profile: UserProfile, platforms: List<SocialPlatform>): ProfileUiData
}
