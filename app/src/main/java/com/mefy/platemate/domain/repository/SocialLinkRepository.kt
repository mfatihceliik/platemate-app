package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.model.social.SocialPlatform

interface SocialLinkRepository {
    suspend fun addSocialLink(platform: String, url: String): AppResult<Unit>
    suspend fun updateSocialLink(link: SocialMediaLink): AppResult<Unit>
    suspend fun deleteSocialLink(id: Long): AppResult<Unit>
    suspend fun getSocialPlatforms(): AppResult<List<SocialPlatform>>
}
