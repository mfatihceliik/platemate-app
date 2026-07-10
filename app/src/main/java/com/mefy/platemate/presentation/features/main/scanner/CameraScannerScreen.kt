package com.mefy.platemate.presentation.features.main.scanner

import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import java.util.concurrent.Executors

@Composable
fun CameraScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: ScannerViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(),
    hasCameraPermission: Boolean,
    onGalleryClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    var camera by remember { mutableStateOf<Camera?>(null) }
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }

    if (hasCameraPermission) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        camera?.let {
                            val currentZoom = it.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                            it.cameraControl.setZoomRatio(currentZoom * zoom)
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        camera?.let { c ->
                            val currentZoom = c.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                            val maxZoom = c.cameraInfo.zoomState.value?.maxZoomRatio ?: 10f
                            val minZoom = c.cameraInfo.zoomState.value?.minZoomRatio ?: 1f
                            val zoomChange = -(dragAmount * 0.01f)
                            val newZoom = (currentZoom + zoomChange).coerceIn(minZoom, maxZoom)
                            c.cameraControl.setZoomRatio(newZoom)
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            previewViewRef?.let { pv ->
                                camera?.let { c ->
                                    val factory = pv.meteringPointFactory
                                    val point = factory.createPoint(offset.x, offset.y)
                                    val action = androidx.camera.core.FocusMeteringAction.Builder(point).build()
                                    c.cameraControl.startFocusAndMetering(action)
                                }
                            }
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
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        
                        val resolutionSelector = ResolutionSelector.Builder()
                            .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
                            .build()
                        
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setResolutionSelector(resolutionSelector)
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                                    viewModel.processImageProxy(imageProxy)
                                }
                            }

                        try {
                            cameraProvider.unbindAll()
                            camera = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                        } catch (exc: Exception) {
                            exc.printStackTrace()
                        }
                    }, executor)

                    previewViewRef = previewView
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                drawRect(Color.Black.copy(alpha = 0.5f))

                val rectWidth = canvasWidth * 0.95f
                val rectHeight = rectWidth * 0.45f
                val rectTopLeftX = (canvasWidth - rectWidth) / 2
                val rectTopLeftY = (canvasHeight - rectHeight) / 2

                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(rectTopLeftX, rectTopLeftY),
                    size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                    blendMode = BlendMode.Clear // İçini temizle (saydam yap)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(
                        width = (context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density * 0.95f).dp,
                        height = (context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density * 0.95f * 0.45f).dp
                    )
                    .border( dims.stroke.st2, if (state.isSuccess) Color.Green else colors.primary, RoundedCornerShape(dims.radius.r16))
            )
            
            if (state.isSuccess && state.detectedPlate != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(dims.radius.r8))
                        .padding(dims.spacing.s16)
                ) {
                    PMText(
                        text = stringResource(R.string.scanner_searching_plate, state.detectedPlate ?: ""),
                        color = Color.Green,
                        fontSize = dims.fontSize.lg,
                        textAlign = TextAlign.Center
                    )
                }
            }

            PMIcon(
                imageVector = Icons.Rounded.PhotoLibrary,
                tint = Color.White,
                size = dims.sizing.iconLg,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dims.spacing.s24)
                    .debouncedClickable {
                        onGalleryClick()
                    }
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    PMCircularProgressIndicator()
                }
            }

            PMText(
                text = stringResource(id = R.string.scanner_hint),
                color = Color.White,
                fontSize = dims.fontSize.md,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dims.spacing.s48)
            )
        }
    }
}
