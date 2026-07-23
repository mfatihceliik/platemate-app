package com.mefy.platemate.presentation.features.main.scanner

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.mefy.platemate.R
import com.mefy.platemate.domain.usecase.scanner.PlateOcrExtractorUseCase
import com.mefy.platemate.presentation.common.global.GlobalUiEventBus
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.viewmodel.BaseViewModel
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val extractPlateUseCase: PlateOcrExtractorUseCase,
    globalUiEventBus: GlobalUiEventBus
) : BaseViewModel(globalUiEventBus) {

    companion object {
        const val ENABLE_SUCCESS_ANIMATION = true
        private const val SUCCESS_ANIMATION_DELAY_MS = 1000L
    }


    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ScannerUiEffect>()
    val uiEffect: SharedFlow<ScannerUiEffect> = _uiEffect.asSharedFlow()

    @Volatile
    private var isProcessingFrame = false


    fun onAction(action: ScannerUiAction) {
        when (action) {
            is ScannerUiAction.OnGalleryImageSelected -> processImageUri(action.uri)
            ScannerUiAction.OnScanStarted -> _uiState.update { it.copy(isScanning = true, errorMessage = null) }
            ScannerUiAction.OnScanStopped -> _uiState.update { it.copy(isScanning = false) }
            is ScannerUiAction.OnError -> _uiState.update { it.copy(errorMessage = action.message, isScanning = false) }
            ScannerUiAction.ClearDetectedPlate -> _uiState.update { it.copy(detectedPlate = null) }
        }
    }

    private fun processImageUri(uri: Uri) {
        launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val plate = extractPlateUseCase.extractPlateFromUri(uri)
            _uiState.update { it.copy(isLoading = false) }
            
            if (plate != null) {
                handleDetectedPlate(plate)
            } else {
                showError(UiText.Resource(R.string.scanner_error_not_found))
            }
        }
    }

    private fun handleDetectedPlate(plate: String) {
        // Zaten bulunmuş bir plaka varsa ve aynıysa yoksay (Canlı tarama için)
        if (_uiState.value.detectedPlate == plate) return
        
        launch {
            _uiState.update { it.copy(detectedPlate = plate, isSuccess = true, isScanning = false) }
            
            if (ENABLE_SUCCESS_ANIMATION) {
                delay(SUCCESS_ANIMATION_DELAY_MS.milliseconds)
            }
            
            _uiEffect.emit(ScannerUiEffect.PlateFound(plate))
        }
    }

    // CameraX için canlı analiz metodu (UI katmanından çağrılacak)
    fun processImageProxy(imageProxy: androidx.camera.core.ImageProxy) {
        // Eğer zaten bir plaka bulduysak, tarama durdurulduysa ya da önceki kare hâlâ işleniyorsa atla
        if (_uiState.value.detectedPlate != null || !_uiState.value.isScanning || isProcessingFrame) {
            imageProxy.close()
            return
        }

        isProcessingFrame = true
        launch {
            try {
                val plate = extractPlateUseCase.extractPlateFromImageProxy(imageProxy)
                if (plate != null) {
                    handleDetectedPlate(plate)
                }
            } finally {
                isProcessingFrame = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        extractPlateUseCase.release()
    }
}
