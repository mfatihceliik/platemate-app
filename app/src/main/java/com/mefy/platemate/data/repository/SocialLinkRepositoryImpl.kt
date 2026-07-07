package com.mefy.platemate.data.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.core.common.result.map
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.mapper.SocialPlatformMapper
import com.mefy.platemate.data.remote.dto.social.AddSocialLinkRequest
import com.mefy.platemate.data.remote.dto.social.UpdateSocialLinkRequest
import com.mefy.platemate.data.remote.rest.service.SocialLinkApiService
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.model.profile.SocialMediaLink
import com.mefy.platemate.domain.model.social.SocialPlatform
import com.mefy.platemate.domain.repository.SocialLinkRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Singleton
class SocialLinkRepositoryImpl @Inject constructor(
    private val api: SocialLinkApiService,
    private val socialPlatformMapper: SocialPlatformMapper,
    private val appDispatchers: AppDispatchers
) : SocialLinkRepository {

    private val platformsCacheMutex = Mutex()
    private var cachedPlatforms: List<SocialPlatform>? = null

    override suspend fun addSocialLink(platform: String, url: String): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeResultCall {
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
            safeResultCall {
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
            safeResultCall { api.deleteSocialLink(id) }
        }

    override suspend fun getSocialPlatforms(): AppResult<List<SocialPlatform>> =
        withContext(appDispatchers.io) {
            platformsCacheMutex.withLock {
                cachedPlatforms?.let { return@withContext AppResult.Success(it) }
                safeApiCall { api.getSocialPlatforms() }
                    .map(socialPlatformMapper::mapList)
                    .also { result -> if (result is AppResult.Success) cachedPlatforms = result.data }
            }
        }
}


