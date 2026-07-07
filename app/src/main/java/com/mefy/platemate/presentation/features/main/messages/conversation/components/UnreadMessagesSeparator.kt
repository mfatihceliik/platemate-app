package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

/** WhatsApp tarzı "N okunmamış mesaj" ayracı; [DateSeparator] ile aynı düzen, primary tonlu. */
@Composable
internal fun UnreadMessagesSeparator(count: Int, modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = colors.primary)
        PMText(
            text = pluralStringResource(R.plurals.conversation_unread_divider, count, count),
            style = PMTextStyle.Note,
            color = colors.primary
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = colors.primary)
    }
}

@Preview(name = "UnreadMessagesSeparator Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun UnreadMessagesSeparatorLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        UnreadMessagesSeparator(count = 3)
    }
}

@Preview(name = "UnreadMessagesSeparator Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun UnreadMessagesSeparatorDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        UnreadMessagesSeparator(count = 12)
    }
}
