package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun MessageInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors
    val primary = colors.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s10)
            .navigationBarsPadding()
            .imePadding(),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant)
                .debouncedClickable {},
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = colors.textLabel,
                modifier = Modifier.size(dims.sizing.iconLg)
            )
        }

        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(colors.surfaceVariant)
                .padding(horizontal = dims.spacing.s16),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.textPrimary),
            cursorBrush = SolidColor(primary),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
                    if (text.isEmpty()) {
                        PMText(
                            text = "Mesaj yaz…",
                            style = PMTextStyle.Body,
                            color = colors.textLabel
                        )
                    }
                    inner()
                }
            }
        )

        Box(
            modifier = Modifier
                .size(38.dp)
                .shadow(elevation = if (text.isNotBlank()) 6.dp else 0.dp, CircleShape)
                .clip(CircleShape)
                .background(if (text.isNotBlank()) primary else colors.surfaceVariant)
                .debouncedClickable(enabled = text.isNotBlank(), onClick = onSend),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null,
                tint = if (text.isNotBlank()) colors.onPrimary else colors.textLabel,
                modifier = Modifier.size(dims.sizing.iconMd)
            )
        }
    }
}

@Preview(name = "MessageInputBar Empty Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun MessageInputBarEmptyLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessageInputBar(text = "", onTextChange = {}, onSend = {})
    }
}

@Preview(name = "MessageInputBar Filled Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun MessageInputBarFilledLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessageInputBar(text = "Merhaba, nasılsınız?", onTextChange = {}, onSend = {})
    }
}

@Preview(name = "MessageInputBar Empty Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun MessageInputBarEmptyDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessageInputBar(text = "", onTextChange = {}, onSend = {})
    }
}

@Preview(name = "MessageInputBar Filled Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun MessageInputBarFilledDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessageInputBar(text = "Merhaba, nasılsınız?", onTextChange = {}, onSend = {})
    }
}
