package com.mefy.platemate.presentation.features.auth.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.mefy.platemate.presentation.components.PMButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMIcon
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.components.variant.PMButtonVariant
import com.mefy.platemate.presentation.components.model.PMTextStyle
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun NotificationPermissionContent(
    onAllowClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing
    val sizing = PMTheme.sizing

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = spacing.s24, vertical = spacing.s48),
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
                    .size(sizing.avatarLg)
                    .background(Color(0xFFFEF9C3), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                PMIcon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = Color(0xFFEAB308),
                )
            }

            Column(
                modifier = Modifier.padding(top = spacing.s24, bottom = spacing.s48),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.s12)
            ) {
                PMText(
                    text = stringResource(R.string.onboarding_notif_title),
                    style = PMTextStyle.Display,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )
                PMText(
                    text = stringResource(R.string.onboarding_notif_desc),
                    style = PMTextStyle.Body,
                    color = colors.textTertiary,
                    textAlign = TextAlign.Center
                )
            }

            // Benefits List
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.s24)
            ) {
                BenefitItem(
                    icon = Icons.Filled.Star,
                    iconBg = Color(0xFFFEF3C7),
                    iconColor = colors.iconStar,
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
            verticalArrangement = Arrangement.spacedBy(spacing.s12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PMButton(
                text = stringResource(R.string.onboarding_notif_allow),
                onClick = onAllowClick,
                modifier = Modifier.fillMaxWidth(),
                variant = PMButtonVariant.Filled
            )

            PMButton(
                text = stringResource(R.string.onboarding_notif_skip),
                onClick = onSkipClick,
                modifier = Modifier.fillMaxWidth(),
                variant = PMButtonVariant.Text
            )
        }
    }
}

@Preview(name = "NotificationPermissionContent Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun NotificationPermissionContentLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        NotificationPermissionContent(onAllowClick = {}, onSkipClick = {})
    }
}

@Preview(name = "NotificationPermissionContent Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun NotificationPermissionContentDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        NotificationPermissionContent(onAllowClick = {}, onSkipClick = {})
    }
}


