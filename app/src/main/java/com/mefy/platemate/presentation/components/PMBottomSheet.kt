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
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PMBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    skipPartiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val spacing = PMTheme.spacing
    val radius = PMTheme.radius
    val colors = PMTheme.colors
    val columnShape = RoundedCornerShape(topStart = radius.r10, topEnd = radius.r10)
    val bottomSheetShape = RoundedCornerShape(topStart = radius.r12, topEnd = radius.r12)

    if (LocalInspectionMode.current) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(columnShape)
                .background(colors.background)
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
        shape = bottomSheetShape,
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
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            PMText(
                text = title,
                style = PMTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.s16, vertical = spacing.s16)
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
    val spacing = PMTheme.spacing

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing.s10)
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
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors

    PMBottomSheet(onDismiss = {}, title = "Başlık") {
        PMText(
            text = "Sheet içeriği buraya gelir.",
            style = PMTextStyle.Body,
            color = colors.textSecondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.s24, vertical = spacing.s16)
        )
        PMBottomSheetActions(
            cancelText = "İptal",
            submitText = "Gönder",
            onCancel = {},
            onSubmit = {},
            modifier = Modifier
                .padding(horizontal = spacing.s24)
                .padding(bottom = spacing.s24)
        )
    }
}
