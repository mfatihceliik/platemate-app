package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.text.UiText
import com.mefy.platemate.presentation.common.text.resolve
import com.mefy.platemate.presentation.components.PMIconButton
import com.mefy.platemate.presentation.components.PMQuotedMessageCard
import com.mefy.platemate.presentation.components.PMTextField
import com.mefy.platemate.presentation.components.variant.PMTextFieldVariant
import com.mefy.platemate.presentation.features.main.messages.conversation.ReplyPreviewUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.theme.PMTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MessageInputBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    replyTarget: ReplyPreviewUiModel? = null,
    onCancelReply: () -> Unit = {}
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val imeVisible = WindowInsets.isImeVisible

    // Exit animasyonu sırasında içerik kalsın diye son gösterileni hatırla.
    var shownReplyTarget by remember { mutableStateOf<ReplyPreviewUiModel?>(null) }
    LaunchedEffect(replyTarget) {
        if (replyTarget != null) shownReplyTarget = replyTarget
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .padding(horizontal = spacing.s12),
            visible = replyTarget != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            shownReplyTarget?.let { target ->
                PMQuotedMessageCard(
                    senderLabel = target.senderLabel.resolve(),
                    contentPreview = target.contentPreview,
                    accentColor = colors.primary,
                    backgroundColor = colors.surface,
                    textColor = colors.textSecondary,
                    trailing = {
                        PMIconButton(
                            imageVector = Icons.Default.Close,
                            onClick = onCancelReply,
                            variant = PMIconButtonVariant.Ghost,
                        )
                    },
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = spacing.s12,
                    end = spacing.s12,
                    top = spacing.s10,
                    bottom = spacing.s10 + if (imeVisible) spacing.s12 else spacing.s0
                ),
            horizontalArrangement = Arrangement.spacedBy(spacing.s8),
            verticalAlignment = Alignment.Bottom
        ) {

            PMIconButton(
                imageVector = Icons.Default.Add,
                onClick = {},
                variant = PMIconButtonVariant.Tonal,
                size = sizing.iconLg
            )

            PMTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                variant = PMTextFieldVariant.Chat,
                maxLines = 5,
                placeholder = stringResource(R.string.conversation_input_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() })
            )

            PMIconButton(
                imageVector = Icons.AutoMirrored.Filled.Send,
                enabled = text.isNotBlank(),
                onClick = onSend,
                variant = PMIconButtonVariant.Tonal,
                size = sizing.iconLg
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

private val messageInputBarPreviewReplyTarget = ReplyPreviewUiModel(
    messageId = 1L,
    senderLabel = UiText.Dynamic("Ayşe Yılmaz"),
    contentPreview = "Yarın saat 5'te müsait misin, buluşabilir miyiz?"
)

@Preview(name = "MessageInputBar Reply Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun MessageInputBarReplyLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        MessageInputBar(
            text = "",
            onTextChange = {},
            onSend = {},
            replyTarget = messageInputBarPreviewReplyTarget,
            onCancelReply = {}
        )
    }
}

@Preview(name = "MessageInputBar Reply Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun MessageInputBarReplyDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        MessageInputBar(
            text = "",
            onTextChange = {},
            onSend = {},
            replyTarget = messageInputBarPreviewReplyTarget,
            onCancelReply = {}
        )
    }
}
