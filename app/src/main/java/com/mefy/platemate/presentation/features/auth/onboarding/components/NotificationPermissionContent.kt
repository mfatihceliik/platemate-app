package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Icon
import com.mefy.platemate.presentation.components.PMButton
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.model.PMButtonStyle
import com.mefy.platemate.presentation.components.model.PMTextStyle

@Composable
fun NotificationPermissionContent(
    onAllowClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = MaterialTheme.pmDimensions.spacing.s24, vertical = MaterialTheme.pmDimensions.spacing.s48),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Bell Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFEF9C3), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFEAB308),
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                modifier = Modifier.padding(top = MaterialTheme.pmDimensions.spacing.s24, bottom = MaterialTheme.pmDimensions.spacing.s48),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s12)
            ) {
                PMText(
                    text = stringResource(R.string.onboarding_notif_title),
                    style = PMTextStyle.Display,
                    color = MaterialTheme.pmColors.textPrimary,
                    textAlign = TextAlign.Center
                )
                PMText(
                    text = stringResource(R.string.onboarding_notif_desc),
                    style = PMTextStyle.Body,
                    color = MaterialTheme.pmColors.textTertiary,
                    textAlign = TextAlign.Center
                )
            }

            // Benefits List
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s24)
            ) {
                BenefitItem(
                    icon = Icons.Filled.Star,
                    iconBg = Color(0xFFFEF3C7),
                    iconColor = MaterialTheme.pmColors.star,
                    title = stringResource(R.string.onboarding_notif_item1_title),
                    desc = stringResource(R.string.onboarding_notif_item1_desc)
                )
                BenefitItem(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    iconBg = Color(0xFFFFEDD5),
                    iconColor = Color(0xFFF97316),
                    title = stringResource(R.string.onboarding_notif_item2_title),
                    desc = stringResource(R.string.onboarding_notif_item2_desc)
                )
                BenefitItem(
                    icon = Icons.Filled.Email,
                    iconBg = Color(0xFFE0F2FE),
                    iconColor = Color(0xFF0EA5E9),
                    title = stringResource(R.string.onboarding_notif_item3_title),
                    desc = stringResource(R.string.onboarding_notif_item3_desc)
                )
            }
        }

        // CTA Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PMButton(
                text = stringResource(R.string.onboarding_notif_allow),
                onClick = onAllowClick,
                modifier = Modifier.fillMaxWidth(),
                style = PMButtonStyle.Filled
            )

            PMButton(
                text = stringResource(R.string.onboarding_notif_skip),
                onClick = onSkipClick,
                modifier = Modifier.fillMaxWidth(),
                style = PMButtonStyle.Text
            )
        }
    }
}

@Composable
private fun BenefitItem(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    desc: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.pmDimensions.spacing.s16)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
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
