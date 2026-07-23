package com.mefy.platemate.presentation.features.main.scanner

sealed interface ScannerUiAction {
    data class OnGalleryImageSelected(val uri: android.net.Uri) : ScannerUiAction
    object OnScanStarted : ScannerUiAction
    object OnScanStopped : ScannerUiAction
    data class OnError(val message: String) : ScannerUiAction
    object ClearDetectedPlate : ScannerUiAction
}