package com.mefy.platemate.presentation.features.auth.sessiongate

data class SessionGateUiState(
    val isLoading: Boolean = true,
    val target: SessionGateTarget? = null
)
