package com.mefy.platemate.presentation.features.admin.reporttypes

import androidx.compose.runtime.Immutable

@Immutable
data class ReportTypeListItem(
    val id: Long,
    val code: String,
    val label: String,
    val severityCode: String,
    val active: Boolean
)
