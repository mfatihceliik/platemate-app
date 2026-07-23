package com.mefy.platemate.presentation.features.main.scanner

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.theme.PlateMateTheme
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.features.main.scanner.components.ScanFrameOverlay
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.features.main.scanner.components.FocusReticule
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CameraScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: ScannerViewModel = hiltViewModel(),
    hasCameraPermission: Boolean,
    onGalleryClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CameraScannerContent(
        modifier = modifier,
        state = state,
        hasCameraPermission = hasCameraPermission,
        onGalleryClick = onGalleryClick,
        onFrame = viewModel::processImageProxy
    )
}

@Composable
internal fun CameraScannerContent(
    modifier: Modifier = Modifier,
    state: ScannerUiState,
    hasCameraPermission: Boolean,
    onGalleryClick: () -> Unit,
    onFrame: (androidx.camera.core.ImageProxy) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val colors = PMTheme.colors
    val animations = PMTheme.animations

    val cameraScannerState = rememberCameraScannerState()
    var focusIndicatorOffset by remember { mutableStateOf<Offset?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (hasCameraPermission) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(cameraScannerState) {
                        detectTransformGestures { _, _, zoom, _ ->
                            cameraScannerState.applyZoomRatio(zoom)
                        }
                    }
                    .pointerInput(cameraScannerState) {
                        detectTapGestures(
                            onTap = { offset ->
                                cameraScannerState.startFocusAndMetering(offset)
                                focusIndicatorOffset = offset
                            }
                        )
                    }
            ) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }
                        cameraScannerState.bindToLifecycle(
                            context = ctx,
                            lifecycleOwner = lifecycleOwner,
                            previewView = previewView,
                            onFrame = onFrame
                        )
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                ScanFrameOverlay(
                    detectedPlate = state.detectedPlate,
                    isSuccess = state.isSuccess,
                    isLoading = state.isLoading,
                    onGalleryClick = onGalleryClick
                )

                focusIndicatorOffset?.let { offset ->
                    LaunchedEffect(offset) {
                        delay(animations.reticleFade.toLong().milliseconds)
                        focusIndicatorOffset = null
                    }
                    FocusReticule(offset = offset, color = colors.primary, fadeDuration = animations.reticleFade)
                }
            }
        } else {
            PMCircularProgressIndicator(fillMaxSize = true, color = colors.primary)
        }
    }
}

@Preview
@Composable
private fun CameraScannerScreenPreview() {
    PlateMateTheme {
        CameraScannerContent(
            state = ScannerUiState(),
            hasCameraPermission = true,
            onGalleryClick = {},
            onFrame = {}
        )
    }
}
