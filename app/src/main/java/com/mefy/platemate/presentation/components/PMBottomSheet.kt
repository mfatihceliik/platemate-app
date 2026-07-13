package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Proje standardı modal bottom sheet: surface arka plan, r16 üst köşeler,
 * opsiyonel sabit başlık + divider bloğu.
 *
 * ModalBottomSheet ayrı bir dialog penceresinde çizildiği için @Preview'da
 * görünmez; inspection mode'da aynı chrome düz composable olarak çizilir.
 * Böylece feature preview'ları bu sarmalayıcıyı doğrudan çağırabilir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (LocalInspectionMode.current) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = dims.radius.r16, topEnd = dims.radius.r16))
                .background(colors.surface)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BottomSheetDefaults.DragHandle()
            }
            PMBottomSheetContent(title = title, content = content)
        }
        return
    }

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        shape = RoundedCornerShape(topStart = dims.radius.r16, topEnd = dims.radius.r16),
        modifier = modifier
    ) {
        PMBottomSheetContent(title = title, content = content)
    }
}

/** Sheet gövdesi: opsiyonel başlık + divider, ardından içerik. */
@Composable
private fun PMBottomSheetContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            PMText(
                text = title,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s16)
            )
            HorizontalDivider(color = colors.surfaceVariant)
        }
        content()
    }
}

/**
 * Standart sheet footer'ı: iptal + destructive submit çifti.
 * [submitEnabled] false iken submit disabled renkte ve tıklanamaz.
 */
@Composable
fun PMBottomSheetActions(
    cancelText: String,
    submitText: String,
    onCancel: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    submitEnabled: Boolean = true
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val ctaShape = MaterialTheme.shapes.small

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(dims.sizing.ctaHeight)
                .clip(ctaShape)
                .background(colors.surfaceVariant)
                .debouncedClickable(onClick = onCancel),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = cancelText,
                style = PMTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.textTertiary
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(dims.sizing.ctaHeight)
                .then(
                    if (submitEnabled) {
                        Modifier.shadow(
                            elevation = 4.dp,
                            shape = ctaShape,
                            ambientColor = colors.error.copy(alpha = 0.5f)
                        )
                    } else {
                        Modifier
                    }
                )
                .clip(ctaShape)
                .background(if (submitEnabled) colors.error else colors.disabled)
                .then(
                    if (submitEnabled) Modifier.debouncedClickable(onClick = onSubmit) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = submitText,
                style = PMTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ── Previews ────────────────────────────────────────────────

@Preview(name = "PMBottomSheet Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMBottomSheetLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMBottomSheetPreviewContent()
    }
}

@Preview(name = "PMBottomSheet Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMBottomSheetDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMBottomSheetPreviewContent()
    }
}

@Composable
private fun PMBottomSheetPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMBottomSheet(onDismiss = {}, title = "Başlık") {
        PMText(
            text = "Sheet içeriği buraya gelir.",
            style = PMTextStyle.Body,
            color = colors.textSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s16)
        )
        PMBottomSheetActions(
            cancelText = "İptal",
            submitText = "Gönder",
            onCancel = {},
            onSubmit = {},
            modifier = Modifier
                .padding(horizontal = dims.spacing.s24)
                .padding(bottom = dims.spacing.s24)
        )
    }
}
