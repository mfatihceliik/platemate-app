package com.mefy.platemate.presentation.features.main.profile.mapper

import com.mefy.platemate.domain.model.profile.UserProfile

interface ProfileUiMapper {
    fun mapProfile(profile: UserProfile): ProfileUiData
}
