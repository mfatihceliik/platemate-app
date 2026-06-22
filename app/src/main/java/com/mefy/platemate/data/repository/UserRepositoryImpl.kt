package com.mefy.platemate.data.repository

import com.mefy.platemate.core.error.AppError
import com.mefy.platemate.core.common.AppResult
import com.mefy.platemate.core.coroutine.AppDispatchers
import com.mefy.platemate.core.common.flatMapSuspend
import com.mefy.platemate.core.common.map
import com.mefy.platemate.core.common.toResultOr
import com.mefy.platemate.data.local.SessionStore
import com.mefy.platemate.data.mapper.UserMapper
import com.mefy.platemate.core.mapper.mapList
import com.mefy.platemate.data.remote.rest.service.UserApiService
import com.mefy.platemate.data.remote.dto.user.UpdateUserRequest
import com.mefy.platemate.data.remote.safeApiCall
import com.mefy.platemate.data.remote.safeMessageCall
import com.mefy.platemate.domain.model.user.User
import com.mefy.platemate.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserRepositoryImpl @Inject constructor(
    private val api: UserApiService,
    private val sessionStore: SessionStore,
    private val userMapper: UserMapper,
    private val appDispatchers: AppDispatchers
) : UserRepository {

    override suspend fun getAllUsers(): AppResult<List<User>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getAllUsers() }.map(userMapper::mapList)
        }

    override suspend fun getUserById(id: Long): AppResult<User> =
        withContext(appDispatchers.io) {
            safeApiCall { api.getUserById(id) }.map(userMapper::map)
        }

    override suspend fun searchByUsername(username: String): AppResult<List<User>> =
        withContext(appDispatchers.io) {
            safeApiCall { api.searchUserByUsername(username) }.map { listOf(userMapper.map(it)) }
        }

    override suspend fun updateCurrentUser(email: String?, password: String?): AppResult<Unit> =
        withContext(appDispatchers.io) {
            currentUserIdResult().flatMapSuspend { userId ->
                safeMessageCall { api.updateCurrentUser(userId, UpdateUserRequest(email, password)) }
            }
        }

    override suspend fun deleteUser(id: Long): AppResult<Unit> =
        withContext(appDispatchers.io) {
            safeMessageCall { api.deleteUser(id) }
        }

    private suspend fun currentUserIdResult(): AppResult<Long> =
        withContext(appDispatchers.io) {
            sessionStore.session.first()?.userId.toResultOr(AppError.SessionExpired)
        }
}



