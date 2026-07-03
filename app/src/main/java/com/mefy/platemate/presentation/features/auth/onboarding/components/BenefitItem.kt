package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun BenefitItem(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    desc: String
) {
    val dims = MaterialTheme.pmDimensions
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        Box(
            modifier = Modifier
                .size(dims.spacing.s48)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            PMIcon(
                imageVector = icon,
                tint = iconColor,
                size = dims.sizing.iconLg,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s4)) {
            PMText(
                text = title,
                style = PMTextStyle.Title,
                color = MaterialTheme.pmColors.textPrimary
            )
            PMText(
                text = desc,
                style = PMTextStyle.Caption,
                color = MaterialTheme.pmColors.textTertiary
            )
        }
    }
}