package com.mefy.platemate.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object OnboardingDestination : AppDestination

@Serializable
data class LoginDestination(
    val prefillIdentifier: String? = null
) : AppDestination

@Serializable
data class RegisterDestination(
    val prefillIdentifier: String? = null
) : AppDestination
