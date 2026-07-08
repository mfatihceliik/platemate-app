package com.mefy.platemate.presentation.features.main.scanner

sealed interface ScannerUiEffect {
    data class PlateFound(val plate: String) : ScannerUiEffect
}