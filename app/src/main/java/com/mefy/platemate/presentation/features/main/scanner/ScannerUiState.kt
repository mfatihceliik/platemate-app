package com.mefy.platemate.presentation.features.main.scanner

data class ScannerUiState(
    val isScanning: Boolean = false,
    val detectedPlate: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)