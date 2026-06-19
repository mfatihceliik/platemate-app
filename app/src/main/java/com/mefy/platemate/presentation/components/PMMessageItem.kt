package com.mefy.platemate.presentation.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun PMMessageItem(
    initials: String,
    name: String,
    preview: String,
    time: String,
    isUnread: Boolean,
    avatarBg: Color,
    avatarFg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .debouncedClickable(onClick = onClick)
            .padding(horizontal = dims.spacing.s16, vertical = dims.spacing.s16),
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s12),
        verticalAlignment = Alignment.CenterVertically
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
                color = avatarFg,
                fontSize = dims.fontSize.lg,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = name,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = preview,
                color = colors.textTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
        ) {
            Text(
                text = time,
                color = colors.textLabel
            )
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .size(dims.sizing.unreadDotSize)
                        .clip(CircleShape)
                        .background(colors.primary)
                )
            }
        }
    }
}

@Preview(name = "PMMessageItem", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PMMessageItemPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PMMessageItem(
                initials = "AY", name = "Ahmet Y.", preview = "Tesekkurler, cok yardimci oldun!",
                time = "09:24", isUnread = true,
                avatarBg = Color(0xFFEEF2FF), avatarFg = Color(0xFF4F46E5),
                onClick = {}
            )
            PMMessageItem(
                initials = "ZK", name = "Zeynep K.", preview = "Plakayi gordum, gercekten nazik biri",
                time = "Dun", isUnread = false,
                avatarBg = Color(0xFFECFEFF), avatarFg = Color(0xFF0891B2),
                onClick = {}
            )
        }
    }
}
