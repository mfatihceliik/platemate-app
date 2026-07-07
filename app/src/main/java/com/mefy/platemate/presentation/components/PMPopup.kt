package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mefy.platemate.presentation.components.model.PMButtonStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Ortalı modal popup: ikon dairesi + başlık + mesaj + 1-2 buton.
 * Başarı/hata/bilgi gibi tüm sonuç ekranları için yeniden kullanılır;
 * renk ve ikonlar parametre olarak verilir (variant'a göre çağıran belirler).
 *
 * [primaryButtonColors] null ise birincil buton tema primary'sini kullanır
 * (hata varyantında kırmızı vermek için override edilir).
 */
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
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = modifier
                .widthIn(max = 320.dp)
                .fillMaxWidth()
                .padding(horizontal = dims.spacing.s24)
                .background(colors.surface, RoundedCornerShape(dims.radius.r24))
                .padding(horizontal = dims.spacing.s24, vertical = dims.spacing.s32),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s24)
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(dims.sizing.avatarXLarge)
                        .background(iconContainerColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    PMIcon(
                        imageVector = icon,
                        tint = iconTint,
                        size = dims.sizing.iconHuge
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                PMText(
                    text = title,
                    fontSize = dims.fontSize.lg,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )
                if (!message.isNullOrBlank()) {
                    PMText(
                        text = message,
                        fontSize = dims.fontSize.sm,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // İsteğe bağlı içerik (örn. input alanı) — başlık ile butonlar arasında.
            content?.invoke(this)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                PMButton(
                    text = primaryText,
                    onClick = onPrimaryClick,
                    colors = primaryButtonColors,
                    modifier = Modifier.fillMaxWidth()
                )
                if (secondaryText != null && onSecondaryClick != null) {
                    PMButton(
                        text = secondaryText,
                        onClick = onSecondaryClick,
                        style = PMButtonStyle.Outlined,
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
        val colors = MaterialTheme.pmColors
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
        val colors = MaterialTheme.pmColors
        PMPopup(
            title = "Yayınlanamadı",
            message = "İnternet bağlantınızı kontrol edip lütfen tekrar deneyin.",
            icon = Icons.Filled.Cancel,
            iconTint = colors.error,
            iconContainerColor = colors.errorContainer,
            primaryText = "Tekrar Dene",
            onPrimaryClick = {},
            primaryButtonColors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
            ),
            secondaryText = "İptal Et",
            onSecondaryClick = {},
            onDismissRequest = {}
        )
    }
}
