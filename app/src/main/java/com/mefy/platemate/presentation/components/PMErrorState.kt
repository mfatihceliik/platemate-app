package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/**
 * Genel ekran-içi hata yer tutucusu: ikon + başlık + [message] + "Tekrar dene".
 * Tüm veri ekranları [PMBaseScreen] üzerinden bunu kullanır; [message] gerçek hata
 * metnini ([com.mefy.platemate.presentation.common.error.toUiText]) taşır.
 */
@Composable
fun PMErrorState(
    message: UiText,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: UiText = UiText.Resource(R.string.common_error_state_title)
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .background(colors.background)
            .padding(dims.spacing.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s12, Alignment.CenterVertically)
    ) {
        PMIcon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            tint = colors.textTertiary,
            size = dims.sizing.iconXl
        )
        PMText(
            text = title.resolve(),
            fontSize = dims.fontSize.lg,
            color = colors.onSurface,
            textAlign = TextAlign.Center
        )
        PMText(
            text = message.resolve(),
            fontSize = dims.fontSize.md,
            color = colors.textTertiary,
            textAlign = TextAlign.Center
        )
        PMButton(
            text = UiText.Resource(R.string.common_retry).resolve(),
            onClick = onRetry
        )
    }
}
