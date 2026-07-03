package com.mefy.platemate.presentation.features.admin.moderation.reports

import androidx.compose.runtime.Immutable

@Immutable
data class CommentReportUiModel(
    val id: Long,
    val plateCode: String,
    val reasonCode: String,
    val description: String,
    val date: String
)
