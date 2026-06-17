package com.mefy.platemate.presentation.features.main.messages.conversation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun DateSeparator(label: String, modifier: Modifier = Modifier) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dims.spacing.s10)
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = colors.cardBorder)
        PMText(text = label, style = PMTextStyle.Note, color = colors.textLabel)
        HorizontalDivider(modifier = Modifier.weight(1f), color = colors.cardBorder)
    }
}
