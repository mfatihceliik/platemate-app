package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.material3.MaterialTheme

@Composable
internal fun MessageRequestBanner(
    enabled: Boolean,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surfaceVariant)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        PMText(
            text = stringResource(R.string.conversation_request_message),
            style = PMTextStyle.Body,
            color = colors.textPrimary
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            PMButton(
                text = stringResource(R.string.conversation_request_decline),
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
