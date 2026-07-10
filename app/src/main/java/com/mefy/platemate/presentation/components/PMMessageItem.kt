package com.mefy.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    name: String,
    preview: String,
    time: String,
    unreadCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUnread = unreadCount > 0
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

        PMAvatar(
            displayName = name,
            size = dims.sizing.avatarMd
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
        ) {
            PMText(
                text = name,
                color = colors.textPrimary,
                fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            PMText(
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
            PMText(
                text = time,
                color = if (isUnread) colors.primary else colors.textLabel
            )
            if (isUnread) {
                // WhatsApp tarzı sayılı rozet; iki+ hanede daireden hap biçimine genişler.
                Box(
                    modifier = Modifier
                        .sizeIn(minWidth = 20.dp, minHeight = 20.dp)
                        .clip(CircleShape)
                        .background(colors.primary)
                        .padding(horizontal = dims.spacing.s4),
                    contentAlignment = Alignment.Center
                ) {
                    PMText(
                        text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                        color = colors.onPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
                name = "Ahmet Y.", preview = "Tesekkurler, cok yardimci oldun!",
                time = "09:24", unreadCount = 3,
                onClick = {}
            )
            PMMessageItem(
                name = "Zeynep K.", preview = "Plakayi gordum, gercekten nazik biri",
                time = "Dun", unreadCount = 0,
                onClick = {}
            )
            PMMessageItem(
                name = "Mehmet C.", preview = "Cok mesaj birikti burada",
                time = "Pzt", unreadCount = 128,
                onClick = {}
            )
        }
    }
}
