package com.mefy.platemate.presentation.features.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.PMTextStyle
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun AuthHeroHeader(
    badgeText: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val dimensions = MaterialTheme.pmDimensions
    val spacing = dimensions.spacing
    val radius = dimensions.radius

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(bottomStart = radius.r18, bottomEnd = radius.r18)
            )
            .padding(horizontal = spacing.s16, vertical = spacing.s20),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.s8)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(radius.r10)
                )
                .border(
                    width = dimensions.stroke.st1,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(radius.r10)
                )
                .padding(horizontal = spacing.s14, vertical = spacing.s8),
            contentAlignment = Alignment.Center
        ) {
            PMText(
                text = badgeText,
                style = PMTextStyle.Headline,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        PMText(
            text = title,
            style = PMTextStyle.Headline,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        PMText(
            text = subtitle,
            style = PMTextStyle.Body,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
