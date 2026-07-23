package com.mefy.platemate.presentation.features.main.scanner.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCircularProgressIndicator
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

private const val SCAN_FRAME_WIDTH_FRACTION = 0.95f
private const val SCAN_FRAME_ASPECT_RATIO = 1f / 0.45f

@Composable
internal fun ScanFrameOverlay(
    detectedPlate: String?,
    isSuccess: Boolean,
    isLoading: Boolean,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val stroke = PMTheme.stroke
    val fontSize = PMTheme.fontSize
    val sizing = PMTheme.sizing
    val radius = PMTheme.radius
    val shapes = PMTheme.shapes
    var frameBounds by remember { mutableStateOf<Rect?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(colors.scrim.copy(alpha = 0.5f))
            frameBounds?.let { rect ->
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = rect.topLeft,
                    size = rect.size,
                    cornerRadius = CornerRadius(radius.r10.toPx(), radius.r10.toPx()),
                    blendMode = BlendMode.Clear
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(SCAN_FRAME_WIDTH_FRACTION)
                .aspectRatio(SCAN_FRAME_ASPECT_RATIO)
                .onGloballyPositioned { coordinates -> frameBounds = coordinates.boundsInParent() }
                .border(
                    width = stroke.st2,
                    color = if (isSuccess) colors.success else colors.primary,
                    shape = shapes.medium
                )
        )

        if (isSuccess && detectedPlate != null) {
            DetectedPlateBadge(
                plate = detectedPlate,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        PMIconButton(
            imageVector = Icons.Rounded.PhotoLibrary,
            onClick = onGalleryClick,
            variant = PMIconButtonVariant.Ghost,
            iconColor = colors.textWhite,
            size = sizing.iconLg,
            contentDescription = stringResource(R.string.scanner_gallery_content_description),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = spacing.s24, vertical = spacing.s48)
        )

        if (isLoading) {
            PMCircularProgressIndicator(
                modifier = Modifier
                    .background(colors.scrim.copy(alpha = 0.5f)),
                fillMaxSize = true,
                color = colors.primary
            )
        }

        PMText(
            text = stringResource(id = R.string.scanner_hint),
            color = colors.textWhite,
            fontSize = fontSize.md,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = spacing.s48)
        )
    }
}

@Preview(name = "ScanFrameOverlay Scanning", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ScanFrameOverlayScanningPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ScanFrameOverlay(
            detectedPlate = null,
            isSuccess = false,
            isLoading = false,
            onGalleryClick = {}
        )
    }
}

@Preview(name = "ScanFrameOverlay Success", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ScanFrameOverlaySuccessPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ScanFrameOverlay(
            detectedPlate = "34 ABC 123",
            isSuccess = true,
            isLoading = false,
            onGalleryClick = {}
        )
    }
}

@Preview(name = "ScanFrameOverlay Loading", showBackground = true, backgroundColor = 0xFF1E293B)
@Composable
private fun ScanFrameOverlayLoadingPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ScanFrameOverlay(
            detectedPlate = null,
            isSuccess = false,
            isLoading = true,
            onGalleryClick = {}
        )
    }
}
