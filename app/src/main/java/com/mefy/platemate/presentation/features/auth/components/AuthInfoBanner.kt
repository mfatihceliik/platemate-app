package com.mefy.platemate.presentation.features.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AuthInfoBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(dimensions.radius.r12)
            )
            .padding(horizontal = spacing.s12, vertical = spacing.s10),
        horizontalArrangement = Arrangement.spacedBy(spacing.s8),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        PMText(
            text = text,
            style = PMTextStyle.Caption,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}
