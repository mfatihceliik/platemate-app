package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SessionGateDestination : AppDestination

@Serializable
data object AuthGraphDestination : AppDestination

@Serializable
data object MainGraphDestination : AppDestination
