package com.mefy.platemate.presentation.features.admin.moderation.comments

import androidx.compose.runtime.Immutable

@Immutable
data class PendingCommentUiModel(
    val id: Long,
    val plateCode: String,
    val username: String,
    val rating: Int,
    val comment: String,
    val reportCount: Int,
    val tags: List<String>,
    val date: String
)