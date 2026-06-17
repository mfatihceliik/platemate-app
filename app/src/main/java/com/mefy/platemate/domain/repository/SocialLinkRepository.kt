package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.domain.model.profile.SocialMediaLink

interface SocialLinkRepository {
    suspend fun addSocialLink(platform: String, url: String): AppResult<Unit>
    suspend fun updateSocialLink(link: SocialMediaLink): AppResult<Unit>
    suspend fun deleteSocialLink(id: Long): AppResult<Unit>
}
