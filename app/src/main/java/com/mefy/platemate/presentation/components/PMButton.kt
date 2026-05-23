package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PMButtonStyle = PMButtonStyle.Filled,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val resolvedEnabled = enabled && !loading
    val buttonModifier = modifier.heightIn(min = 48.dp)

    when (style) {
        PMButtonStyle.Filled -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = resolvedEnabled
            ) {
                PMButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon
                )
            }
        }
        PMButtonStyle.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = resolvedEnabled
            ) {
                PMButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon
                )
            }
        }
        PMButtonStyle.Text -> {
            TextButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = resolvedEnabled
            ) {
                PMButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon
                )
            }
        }
    }
}

@Composable
private fun PMButtonContent(
    text: String,
    loading: Boolean,
    leadingIcon: @Composable (() -> Unit)?
) {
    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            strokeWidth = MaterialTheme.pmDimensions.stroke.st2,
            color = LocalContentColor.current
        )
        return
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.invoke()
        PMText(text = text, style = PMTextStyle.Label, color = LocalContentColor.current)
    }
}

@Preview(name = "PMButton Light", showBackground = true, backgroundColor = 0xFFF6FAFB)
@Composable
private fun PMButtonLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        PMButtonPreviewContent()
    }
}

@Preview(name = "PMButton Dark", showBackground = true, backgroundColor = 0xFF101618)
@Composable
private fun PMButtonDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        PMButtonPreviewContent()
    }
}

@Composable
private fun PMButtonPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.pmDimensions.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s10)
    ) {
        PMButton(
            text = "Filled",
            onClick = {},
            style = PMButtonStyle.Filled,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Outlined",
            onClick = {},
            style = PMButtonStyle.Outlined,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Text",
            onClick = {},
            style = PMButtonStyle.Text,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Loading",
            onClick = {},
            loading = true,
            modifier = Modifier.fillMaxWidth()
        )
        PMButton(
            text = "Disabled",
            onClick = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
