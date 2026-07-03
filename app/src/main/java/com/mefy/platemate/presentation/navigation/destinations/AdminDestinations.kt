package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object AdminGraphDestination : AppDestination

@Serializable
data object AdminHubDestination : AppDestination

@Serializable
data object AdminCommentModerationDestination : AppDestination

@Serializable
data object AdminCommentReportsDestination : AppDestination

@Serializable
data object AdminPlateRemovalDestination : AppDestination

@Serializable
data object AdminHiddenPlatesDestination : AppDestination

@Serializable
data object AdminSettingsDestination : AppDestination

@Serializable
data object AdminReportTypesDestination : AppDestination

@Serializable
data class AdminReportTypeFormDestination(val typeId: Long = -1L) : AppDestination

@Serializable
data object AdminSocialPlatformsDestination : AppDestination

@Serializable
data class AdminSocialPlatformFormDestination(val platformId: Long = -1L) : AppDestination
