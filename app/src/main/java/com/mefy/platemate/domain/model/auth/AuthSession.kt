package com.mefy.platemate.domain.model.auth

data class AuthSession(
    val userId: Long,
    val username: String,
    val token: String,
    val refreshToken: String? = null
)
