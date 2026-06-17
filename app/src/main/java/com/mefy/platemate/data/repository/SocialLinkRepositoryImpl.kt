package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.remote.dto.social.AddSocialLinkRequest
import com.mefy.platemate.data.remote.dto.social.UpdateSocialLinkRequest
import com.mefy.platemate.data.remote.rest.service.SocialLinkApiService
import com.mefy.platemate.data.remote.safeMessageCall
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.repository.SocialLinkRepository
import javax.inject.Inject
import kotlinx.coroutines.withContext

class SocialLinkRepositoryImpl @Inject constructor(
    private val api: SocialLinkApiService,
    private val appDispatchers: AppDispatchers
) : SocialLinkRepository {

    override suspend fun addSocialLink(platform: String, url: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall {
                api.addSocialLink(
                    AddSocialLinkRequest(
                        platformCode = platform,
                        url = url
                    )
                )
            }
        }

    override suspend fun updateSocialLink(link: SocialMediaLink): AppResult<Unit> =
        withContext(appDispatchers.io) {
            val id = link.id ?: return@withContext AppResult.Success(Unit)
            safeMessageCall {
                api.updateSocialLink(
                    UpdateSocialLinkRequest(
                        id = id,
                        platformCode = link.platform,
                        url = link.url
                    )
                )
            }
        }

    override suspend fun deleteSocialLink(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.deleteSocialLink(id) }
        }
}


