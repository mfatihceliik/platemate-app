package com.mefy.platemate.domain.model.user

data class UserSearchResult(
    val id: Long,
    val username: String,
    val displayName: String?,
    val bio: String?,
    val profilePhotoUrl: String?
)
