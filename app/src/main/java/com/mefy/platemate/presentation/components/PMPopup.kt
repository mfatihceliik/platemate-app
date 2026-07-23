package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMPopup(
    modifier: Modifier = Modifier,
    title: String,
    message: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = Color.Unspecified,
    iconContainerColor: Color = Color.Transparent,
    primaryText: String,
    onPrimaryClick: () -> Unit,
    onDismissRequest: () -> Unit,
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    primaryButtonColors: ButtonColors? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing
    val fontSize = PMTheme.fontSize
    val colors = PMTheme.colors
    val shape = PMTheme.shapes.medium

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = modifier
                .widthIn(max = sizing.popupSize)
                .fillMaxWidth()
                .padding(horizontal = spacing.s24)
                .background(colors.surface, shape)
                .padding(horizontal = spacing.s24, vertical = spacing.s32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.s24)
        ) {
            if (icon != null) {
                PMIconContainer(
                    imageVector = icon,
                    tint = iconTint,
                    iconSize = sizing.iconLg,
                    containerSize = sizing.iconContainer,
                    containerColor = iconContainerColor
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                PMText(
                    text = title,
                    fontSize = fontSize.lg,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )
                if (!message.isNullOrBlank()) {
                    PMText(
                        text = message,
                        fontSize = fontSize.sm,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // İsteğe bağlı içerik (örn. input alanı) — başlık ile butonlar arasında.
            content?.invoke(this)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                PMButton(
                    text = primaryText,
                    onClick = onPrimaryClick,
                    buttonColors = primaryButtonColors,
                    modifier = Modifier.fillMaxWidth()
                )
                if (secondaryText != null && onSecondaryClick != null) {
                    PMButton(
                        text = secondaryText,
                        onClick = onSecondaryClick,
                        variant = PMButtonVariant.Outlined,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ── Previews ────────────────────────────────────────────────

@Preview(name = "PMPopup Success", showBackground = true, backgroundColor = 0xFF64748B)
@Composable
private fun PMPopupSuccessPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = PMTheme.colors
        PMPopup(
            title = "Başarıyla Paylaşıldı!",
            message = "Değerlendirmen toplulukla paylaşıldı. Teşekkürler!",
            icon = Icons.Filled.CheckCircle,
            iconTint = colors.success,
            iconContainerColor = colors.categoryGreenBg,
            primaryText = "Ana Sayfaya Git",
            onPrimaryClick = {},
            secondaryText = "Başka Araç Puanla",
            onSecondaryClick = {},
            onDismissRequest = {}
        )
    }
}

@Preview(name = "PMPopup Error", showBackground = true, backgroundColor = 0xFF64748B)
@Composable
private fun PMPopupErrorPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val colors = PMTheme.colors
        PMPopup(
            title = "Yayınlanamadı",
            message = "İnternet bağlantınızı kontrol edip lütfen tekrar deneyin.",
            icon = Icons.Filled.Cancel,
            iconTint = colors.error,
            iconContainerColor = colors.errorContainer,
            primaryText = "Tekrar Dene",
            onPrimaryClick = {},
            primaryButtonColors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            secondaryText = "İptal Et",
            onSecondaryClick = {},
            onDismissRequest = {}
        )
    }
}
