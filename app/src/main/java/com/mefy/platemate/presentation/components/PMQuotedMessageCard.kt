package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.variant.PMIconButtonVariant
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
fun PMQuotedMessageCard(
    senderLabel: String,
    contentPreview: String,
    accentColor: Color,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null
) {
    val spacing = PMTheme.spacing
    val colors = PMTheme.colors
    val shape = PMTheme.shapes.medium

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .drawBehind {
                drawRect(
                    color = colors.primary,
                    size = Size(spacing.s8.toPx(), size.height)
                )
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(spacing.s8)
                .padding(horizontal = spacing.s4),
            verticalArrangement = Arrangement.spacedBy(spacing.s8),
            horizontalAlignment = Alignment.Start
        ) {
            PMText(
                text = senderLabel,
                style = PMTextStyle.Caption,
                color = accentColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            PMText(
                text = contentPreview,
                style = PMTextStyle.Caption,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (trailing != null) {
            Box(modifier = Modifier
                .padding(end = spacing.s4)) {
                trailing()
            }
        }
    }
}

@Preview(name = "PMQuotedMessageCard In-Bubble Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMQuotedMessageCardInBubbleLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val spacing = PMTheme.spacing
        val colors = PMTheme.colors
        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            PMQuotedMessageCard(
                senderLabel = "Ahmet Yılmaz",
                contentPreview = "Merhaba, plakamı gördünüz mü?",
                accentColor = colors.primary,
                backgroundColor = colors.primary.copy(alpha = 0.1f),
                textColor = colors.textPrimary
            )
        }
    }
}

@Preview(name = "PMQuotedMessageCard With Trailing Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMQuotedMessageCardWithTrailingDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val spacing = PMTheme.spacing
        val colors = PMTheme.colors
        Column(
            modifier = Modifier.padding(spacing.s16),
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            PMQuotedMessageCard(
                senderLabel = "Ayşe Yılmaz",
                contentPreview = "Yarın saat 5'te müsait misin, buluşabilir miyiz?",
                accentColor = colors.primary,
                backgroundColor = colors.surface,
                textColor = colors.textSecondary,
                trailing = {
                    PMIconButton(
                        imageVector = Icons.Default.Close,
                        onClick = {},
                        variant = PMIconButtonVariant.Ghost
                    )
                }
            )
        }
    }
}
