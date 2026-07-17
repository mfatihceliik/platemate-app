package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    skipPartiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    if (LocalInspectionMode.current) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = dims.radius.r10, topEnd = dims.radius.r10))
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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

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
                    .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s16)
            )
            HorizontalDivider(color = colors.surfaceVariant)
        }
        content()
    }
}

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

        PMButton(
            text = cancelText,
            onClick = onCancel,
            variant = PMButtonVariant.Outlined,
            modifier = Modifier.weight(1f)
        )

        PMButton(
            text = submitText,
            onClick = onSubmit,
            variant = PMButtonVariant.Filled,
            enabled = submitEnabled,
            modifier = Modifier.weight(1f)
        )
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
