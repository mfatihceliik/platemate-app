package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMUserCard(
    displayName: String,
    modifier: Modifier = Modifier,
    username: String? = null,
    subtitle: String? = null,
    avatarSize: Dp = MaterialTheme.pmDimensions.sizing.avatarMd,
    containerColor: Color = MaterialTheme.pmColors.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null
) {
    val colors = MaterialTheme.pmColors
    val dims = MaterialTheme.pmDimensions

    val clickModifier = if (onClick != null) {
        Modifier.debouncedClickable(onClick = onClick)
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor)
            .then(clickModifier)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMAvatar(
            displayName = displayName,
            size = avatarSize
        )
        
        Spacer(modifier = Modifier.width(dims.spacing.s12))
        
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = displayName,
                style = PMTextStyle.Body,
                fontWeight = FontWeight.SemiBold,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!username.isNullOrBlank()) {
                PMText(
                    text = username,
                    style = PMTextStyle.Note,
                    color = colors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (!subtitle.isNullOrBlank()) {
                PMText(
                    text = subtitle,
                    style = PMTextStyle.Caption,
                    color = colors.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        
        if (trailing != null) {
            Spacer(modifier = Modifier.width(dims.spacing.s12))
            trailing()
        }
    }
}

@Preview(name = "PMUserCard Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun PMUserCardLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(modifier = Modifier.padding(dims.spacing.s16), verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            PMUserCard(
                displayName = "Ahmet Yılmaz",
                username = "@ahmetyilmaz",
                onClick = {}
            )
            PMUserCard(
                displayName = "Caner Yıldırım",
                onClick = {}
            )
            PMUserCard(
                displayName = "Ayşe Kaya",
                username = "@aysekaya",
                onClick = {},
                trailing = {
                    PMText(text = "Follow", color = MaterialTheme.pmColors.primary, fontWeight = FontWeight.Bold)
                }
            )
        }
    }
}

@Preview(name = "PMUserCard Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun PMUserCardDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        val dims = MaterialTheme.pmDimensions
        Column(modifier = Modifier.padding(dims.spacing.s16), verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)) {
            PMUserCard(
                displayName = "Ahmet Yılmaz",
                username = "@ahmetyilmaz",
                onClick = {}
            )
            PMUserCard(
                displayName = "Caner Yıldırım",
                onClick = {}
            )
            PMUserCard(
                displayName = "Ayşe Kaya",
                username = "@aysekaya",
                onClick = {},
                trailing = {
                    PMText(text = "Follow", color = MaterialTheme.pmColors.primary, fontWeight = FontWeight.Bold)
                }
            )
        }
    }
}
