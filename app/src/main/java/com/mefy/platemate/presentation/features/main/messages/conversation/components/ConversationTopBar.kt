package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.common.topbar.PMBackButton
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun ConversationTopBar(
    modifier: Modifier = Modifier,
    participantName: String,
    initials: String,
    avatarBg: Color,
    avatarFg: Color,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    isOnline: Boolean? = null
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    // Status-bar inset + zemin PMTopBar (Custom yolu) tarafından sağlanır; burada yok.
    // WhatsApp tarzı düz satır: kart/pill yok; avatar + isim + durum alt yazısı,
    // satırın tamamı tıklanınca sohbet bilgilerine gider.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(horizontal = dims.spacing.s8, vertical = dims.spacing.s8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        PMBackButton(onBackClick)

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.medium)
                .debouncedClickable(onClick = onInfoClick)
                .padding(horizontal = dims.spacing.s4, vertical = dims.spacing.s4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            Box(
                modifier = Modifier
                    .size(dims.sizing.avatarMedium)
                    .clip(CircleShape)
                    .background(avatarBg),
                contentAlignment = Alignment.Center
            ) {
                PMText(
                    text = initials,
                    style = PMTextStyle.Caption,
                    fontWeight = FontWeight.Bold,
                    color = avatarFg
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)) {
                PMText(
                    text = participantName,
                    style = PMTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (isOnline != null) {
                    PMText(
                        text = stringResource(
                            if (isOnline) R.string.conversation_online
                            else R.string.conversation_offline
                        ),
                        style = PMTextStyle.Caption,
                        color = if (isOnline) colors.success else colors.textLabel
                    )
                }
            }
        }
    }
}

@Preview(name = "ConversationTopBar Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ConversationTopBarLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ConversationTopBar(
            participantName = "Ahmet Yılmaz",
            initials = "AY",
            avatarBg = Color(0xFFECFEFF),
            avatarFg = Color(0xFF0E7490),
            onBackClick = {},
            onInfoClick = {},
            isOnline = true
        )
    }
}

@Preview(name = "ConversationTopBar Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ConversationTopBarDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ConversationTopBar(
            participantName = "Ahmet Yılmaz",
            initials = "AY",
            avatarBg = Color(0xFF164E63),
            avatarFg = Color(0xFF67E8F9),
            onBackClick = {},
            onInfoClick = {},
            isOnline = false
        )
    }
}
