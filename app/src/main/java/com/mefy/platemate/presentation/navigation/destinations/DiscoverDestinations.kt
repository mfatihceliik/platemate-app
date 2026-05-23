package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DiscoverDestination : AppDestination

@Serializable
data class DiscoverDetailDestination(
    val id: String
) : AppDestination
