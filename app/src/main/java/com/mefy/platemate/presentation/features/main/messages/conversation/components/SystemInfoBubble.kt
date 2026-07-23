package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun SystemInfoBubble(
    modifier: Modifier = Modifier,
    text: String
) {
    val colors = PMTheme.colors
    val sizing = PMTheme.sizing
    val spacing = PMTheme.spacing
    val shape = PMTheme.shapes

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.warning.copy(alpha = 0.12f), shape.medium)
            .padding(horizontal = spacing.s8, vertical = spacing.s8),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PMIcon(
            imageVector = Icons.Filled.Info,
            tint = colors.iconWarning,
            size = sizing.iconLg
        )
        PMText(
            text = text,
            style = PMTextStyle.Note,
            color = colors.warning
        )
    }
}

@Preview(name = "SystemInfoBubble Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun SystemInfoBubbleLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        SystemInfoBubble(text = "Bu kullanıcı onaylamadan daha fazla mesaj gönderemezsiniz.")
    }
}

@Preview(name = "SystemInfoBubble Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun SystemInfoBubbleDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        SystemInfoBubble(text = "Bu kullanıcı onaylamadan daha fazla mesaj gönderemezsiniz.")
    }
}
