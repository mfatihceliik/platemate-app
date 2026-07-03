package com.mefy.platemate.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.data.remote.dto.fcm.RegisterFcmTokenRequest
import com.mefy.platemate.data.remote.rest.service.FcmTokenApiService
import com.mefy.platemate.data.remote.safeResultCall
import com.mefy.platemate.domain.repository.FcmTokenRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.withContext

class FcmTokenRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val api: FcmTokenApiService,
    private val appDispatchers: AppDispatchers
) : FcmTokenRepository {

    override suspend fun registerToken(token: String) =
        withContext(appDispatchers.io) {
            safeResultCall { api.registerFcmToken(RegisterFcmTokenRequest(token = token, deviceId = deviceId())) }
        }

    override suspend fun unregisterToken(token: String) =
        withContext(appDispatchers.io) {
            safeResultCall { api.unregisterFcmToken(token) }
        }

    @SuppressLint("HardwareIds")
    private fun deviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).orEmpty()
}
