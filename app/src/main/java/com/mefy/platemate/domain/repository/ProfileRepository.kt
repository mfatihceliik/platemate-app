package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.profile.UserProfile

interface ProfileRepository {
    suspend fun getProfile(userId: Long): AppResult<UserProfile>
}


