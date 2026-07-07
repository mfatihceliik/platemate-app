package com.mefy.platemate.presentation.features.uimodel

import androidx.compose.runtime.Immutable

@Immutable
data class FriendRequestNotificationItem(
    override val id: String,
    val friendUserId: Long,
    val username: String,
    val statusCode: String,
    override val createdAtText: String,
    override val sortKey: String
) : ProfileActivityUiModel
