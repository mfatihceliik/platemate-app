package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.model.PMTextFieldVariant
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@OptIn(ExperimentalLayoutApi::class)
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
    // Klavye açıkken bar, IME'ye yapışık durmasın diye altına s12 boşluk eklenir; kapalıyken
    // yalnızca nav-bar boşluğu kalır (root ime padding'i PMBaseScreen.applyImePadding'te uygulanır).
    val imeVisible = WindowInsets.isImeVisible

    Row(
        modifier = modifier
            .fillMaxWidth()
            // Yarı saydam "buzlu cam": bar listenin üzerine biner, mesajlar arkasından
            // görünür. (Gerçek gaussian backdrop-blur bir bağımlılık/RenderEffect gerektirir;
            // minSdk 24'te güvenli değil — şeffaflık her API'de çalışır.)
            .background(colors.surface.copy(alpha = 0.82f))
            // IME inset'i ekran kökünde (PMBaseScreen.applyImePadding) uygulanır; burada
            // yalnızca nav-bar boşluğu kalır. İkisi birden uygulanırsa çifte padding olur.
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(
                start = dims.spacing.s12,
                end = dims.spacing.s12,
                top = dims.spacing.s10,
                bottom = dims.spacing.s10 + if (imeVisible) dims.spacing.s12 else dims.spacing.s0
            ),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10),
        // Alan çok satıra büyürken +/gönder butonları altta sabit kalır (WhatsApp davranışı).
        verticalAlignment = Alignment.Bottom
    ) {
        Box(
            modifier = Modifier
                .size(dims.sizing.chatComposerButton)
                .clip(CircleShape)
                .background(colors.surfaceVariant)
                .debouncedClickable {},
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.Default.Add,
                tint = colors.textLabel,
                size = dims.sizing.iconLg
            )
        }

        // Chat pill: paylaşılan PMTextField'ın Chat variant'ı (kenarlıksız, hap biçimli).
        PMTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            variant = PMTextFieldVariant.Chat,
            // Uzun metin alt satıra sarar; 5 satırdan sonra alan büyümez, metin içeride kayar.
            maxLines = 5,
            placeholder = stringResource(R.string.conversation_input_placeholder),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() })
        )

        Box(
            modifier = Modifier
                .size(dims.sizing.chatComposerButton)
                .shadow(elevation = if (text.isNotBlank()) dims.spacing.s8 else dims.spacing.s0, CircleShape)
                .clip(CircleShape)
                .background(if (text.isNotBlank()) primary else colors.surfaceVariant)
                .debouncedClickable(enabled = text.isNotBlank(), onClick = onSend),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                tint = if (text.isNotBlank()) colors.onPrimary else colors.textLabel,
                size = dims.sizing.iconMd,
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

@Preview(name = "MessageInputBar Multiline Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun MessageInputBarMultilineLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessageInputBar(
            text = "Bu uzun bir mesaj örneği. Metin alanı beş satıra kadar büyür, " +
                "sonrasında içeride kaydırılır. Gönder butonu altta sabit kalır.",
            onTextChange = {},
            onSend = {}
        )
    }
}
