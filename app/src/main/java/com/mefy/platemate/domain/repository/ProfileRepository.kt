package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.profile.ProfilePage
import com.mefy.platemate.domain.model.profile.UserProfile

interface ProfileRepository {
    suspend fun getProfile(userId: Long): AppResult<UserProfile>
    suspend fun getProfilePage(userId: Long): AppResult<ProfilePage>
    suspend fun updateProfile(
        userId: Long,
        displayName: String?,
        username: String?,
        bio: String?,
        profilePhotoUrl: String?
    ): AppResult<Unit>
}


