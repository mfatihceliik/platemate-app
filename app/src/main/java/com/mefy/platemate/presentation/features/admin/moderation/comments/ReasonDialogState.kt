package com.mefy.platemate.presentation.features.admin.moderation.comments

import androidx.compose.runtime.Immutable

@Immutable
data class ReasonDialogState(
    val commentId: Long,
    val action: ModerationAction,
    val reason: String = ""
)