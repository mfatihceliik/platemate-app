package com.mefy.platemate.presentation.features.main.scanner

import android.content.Context
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * ML Kit reads a Turkish plate fine well below full sensor resolution; capping the analysis
 * stream keeps every OCR pass fast and avoids feeding oversized frames into the native pipeline.
 */
private val ANALYSIS_TARGET_RESOLUTION = Size(1280, 720)

@Stable
class CameraScannerState internal constructor(
    private val analysisExecutor: ExecutorService
) {
    var camera by mutableStateOf<Camera?>(null)
        private set

    private var previewView: PreviewView? = null
    private var cameraProvider: ProcessCameraProvider? = null

    fun bindToLifecycle(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onFrame: (ImageProxy) -> Unit
    ) {
        this.previewView = previewView
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            // .get() can throw (ExecutionException/IllegalStateException) if a still-in-flight
            // unbind from a previous session races this bind — handled instead of crashing.
            try {
                val provider = cameraProviderFuture.get()
                cameraProvider = provider

                val preview = Preview.Builder()
                    .setResolutionSelector(
                        ResolutionSelector.Builder()
                            .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
                            .build()
                    )
                    .build()
                    .also { it.surfaceProvider = previewView.surfaceProvider }

                val analysisResolution = ResolutionSelector.Builder()
                    .setResolutionStrategy(
                        ResolutionStrategy(
                            ANALYSIS_TARGET_RESOLUTION,
                            ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                        )
                    )
                    .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setResolutionSelector(analysisResolution)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { it.setAnalyzer(analysisExecutor, onFrame) }

                provider.unbindAll()
                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun applyZoomRatio(factor: Float) {
        val activeCamera = camera ?: return
        val currentZoom = activeCamera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
        val minZoom = activeCamera.cameraInfo.zoomState.value?.minZoomRatio ?: 1f
        val maxZoom = activeCamera.cameraInfo.zoomState.value?.maxZoomRatio ?: 1f
        activeCamera.cameraControl.setZoomRatio((currentZoom * factor).coerceIn(minZoom, maxZoom))
    }

    fun startFocusAndMetering(offset: Offset) {
        val activeCamera = camera ?: return
        val pv = previewView ?: return
        val point = pv.meteringPointFactory.createPoint(offset.x, offset.y)
        activeCamera.cameraControl.startFocusAndMetering(FocusMeteringAction.Builder(point).build())
    }

    /** Deterministic teardown on navigate-back, so a fresh bind on next entry doesn't race the
     * previous session's async unbind (ProcessCameraProvider is a process-wide singleton). */
    fun release() {
        runCatching { cameraProvider?.unbindAll() }
        analysisExecutor.shutdown()
    }
}

@Composable
fun rememberCameraScannerState(): CameraScannerState {
    val state = remember { CameraScannerState(Executors.newSingleThreadExecutor()) }
    DisposableEffect(state) {
        onDispose { state.release() }
    }
    return state
}
