package com.mefy.platemate.presentation.features.main.messages.chatdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.components.util.debouncedClickable
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun QuickAction(
    label: String,
    icon: ImageVector,
    bg: Color,
    tint: Color,
    onClick: () -> Unit
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s4)
    ) {
        PMIcon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            size = dims.sizing.iconLg,
            containerColor = bg,
            modifier = Modifier.debouncedClickable(onClick = onClick)
        )

        PMText(text = label, style = PMTextStyle.Note, color = colors.textLabel)
    }
}