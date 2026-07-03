package com.mefy.platemate.domain.repository

import com.mefy.platemate.core.common.result.AppResult
import com.mefy.platemate.domain.model.user.User

interface UserRepository {
    suspend fun getAllUsers(): AppResult<List<User>>
    suspend fun getUserById(id: Long): AppResult<User>
    suspend fun searchByUsername(username: String): AppResult<List<User>>
    suspend fun updateCurrentUser(email: String?, password: String?): AppResult<Unit>
    suspend fun deleteUser(id: Long): AppResult<Unit>
    suspend fun deleteCurrentUser(): AppResult<Unit>
}

