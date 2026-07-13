package com.mefy.platemate.presentation.features.admin.commentreasons

import androidx.compose.runtime.Immutable

@Immutable
data class CommentReasonListItem(
    val id: Long,
    val code: String,
    val label: String,
    val requiresDescription: Boolean,
    val active: Boolean
)
