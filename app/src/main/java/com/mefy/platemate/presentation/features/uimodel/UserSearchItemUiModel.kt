package com.mefy.platemate.presentation.features.uimodel

data class UserSearchItemUiModel(
    val id: Long,
    val username: String,
    val displayName: String? = null,
    val bio: String? = null
)
