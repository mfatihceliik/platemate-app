package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.theme.PMTheme

@Composable
internal fun MessageRequestBanner(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant)
            .padding(horizontal = spacing.s16, vertical = spacing.s12),
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            PMIcon(
                imageVector = Icons.Filled.Info,
                tint = colors.iconWarning,
                size = sizing.iconLg
            )
            PMText(
                text = stringResource(R.string.conversation_request_message),
                style = PMTextStyle.Body,
                color = colors.textPrimary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8)
        ) {
            PMButton(
                text = stringResource(R.string.conversation_request_decline),
                buttonColors = ButtonColors(
                    containerColor = colors.error,
                    contentColor = colors.onError,
                    disabledContainerColor = colors.error.copy(alpha = 0.12f),
                    disabledContentColor = colors.onError.copy(alpha = 0.38f)
                ),
                onClick = onDecline,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )
            PMButton(
                text = stringResource(R.string.conversation_request_accept),
                onClick = onAccept,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(name = "MessageRequestBanner Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessageRequestBannerLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessageRequestBanner(enabled = true, onAccept = {}, onDecline = {})
    }
}

@Preview(name = "MessageRequestBanner Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun MessageRequestBannerDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessageRequestBanner(enabled = true, onAccept = {}, onDecline = {})
    }
}

@Preview(name = "MessageRequestBanner Disabled", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun MessageRequestBannerDisabledPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessageRequestBanner(enabled = false, onAccept = {}, onDecline = {})
    }
}
