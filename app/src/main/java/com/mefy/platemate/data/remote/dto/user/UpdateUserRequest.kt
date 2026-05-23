package com.mefy.platemate.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)