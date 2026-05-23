package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SearchDestination : AppDestination

@Serializable
data class SearchDetailDestination(
    val id: String
) : AppDestination
