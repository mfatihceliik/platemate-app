package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DiscoverDestination : AppDestination

@Serializable
data class DiscoverDetailDestination(
    val id: String
) : AppDestination

@Serializable
data class DiscoverCityPlatesDestination(
    val cityId: Int,
    val cityName: String
) : AppDestination

@Serializable
data object DiscoverFilterDestination : AppDestination

@Serializable
data object DiscoverCityFilterDestination : AppDestination

@Serializable
data object DiscoverReportTypeFilterDestination : AppDestination
