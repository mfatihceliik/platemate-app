package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object SearchGraphDestination : AppDestination

@Serializable
data object DiscoverGraphDestination : AppDestination

@Serializable
data object MessagesGraphDestination : AppDestination

@Serializable
data object ProfileGraphDestination : AppDestination
