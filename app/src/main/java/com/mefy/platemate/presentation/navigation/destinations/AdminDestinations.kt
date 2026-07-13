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
data object AdminCommentReasonsDestination : AppDestination

@Serializable
data class AdminCommentReasonFormDestination(val reasonId: Long = -1L) : AppDestination

@Serializable
data object AdminSocialPlatformsDestination : AppDestination

@Serializable
data class AdminSocialPlatformFormDestination(val platformId: Long = -1L) : AppDestination

@Serializable
data object AdminPremiumPlansDestination : AppDestination

@Serializable
data class AdminPremiumPlanFormDestination(val planId: Long = -1L) : AppDestination

@Serializable
data object AdminPremiumFeaturesDestination : AppDestination

@Serializable
data class AdminPremiumFeatureFormDestination(val featureId: Long = -1L) : AppDestination

@Serializable
data object AdminAccentColorsDestination : AppDestination

@Serializable
data class AdminAccentColorFormDestination(val colorId: Long = -1L) : AppDestination
