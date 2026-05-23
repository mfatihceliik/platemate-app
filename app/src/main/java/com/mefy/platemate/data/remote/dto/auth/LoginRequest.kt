package com.mefy.platemate.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String
) {
    companion object {
        fun fromIdentifier(identifier: String, password: String): LoginRequest {
            val trimmedIdentifier = identifier.trim()
            return if ("@" in trimmedIdentifier) {
                LoginRequest(username = null, email = trimmedIdentifier, password = password)
            } else {
                LoginRequest(username = trimmedIdentifier, email = null, password = password)
            }
        }
    }
}
