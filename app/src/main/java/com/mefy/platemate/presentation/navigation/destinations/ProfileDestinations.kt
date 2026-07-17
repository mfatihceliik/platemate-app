package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ProfileDestination : AppDestination

@Serializable
data object EditProfileDestination : AppDestination

@Serializable
data object SettingsHomeDestination : AppDestination

@Serializable
data object ProfileNotificationPreferencesDestination : AppDestination

@Serializable
data object ProfilePremiumInfoDestination : AppDestination

@Serializable
data class ProfileFriendsDestination(val initialTab: Int = 0) : AppDestination

@Serializable
data object ProfileChangePasswordDestination : AppDestination

@Serializable
data object ProfileThemeColorDestination : AppDestination

@Serializable
data object ProfileCardStyleDestination : AppDestination

@Serializable
data object ProfileLanguageDestination : AppDestination

@Serializable
data class UserProfileDestination(val userId: String) : AppDestination

@Serializable
data class ProfileReviewListDestination(val initialStatus: String) : AppDestination
