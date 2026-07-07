package com.mefy.platemate.presentation.features.admin.hub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMRowItem
import com.mefy.platemate.presentation.components.PMRowPosition
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
internal fun AdminHubScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    onPendingComments: () -> Unit,
    onCommentReports: () -> Unit,
    onPlateRemoval: () -> Unit,
    onHiddenPlates: () -> Unit,
    onReportTypes: () -> Unit,
    onSocialPlatforms: () -> Unit,
    onPremiumPlans: () -> Unit,
    onPremiumFeatures: () -> Unit,
    onThemeColors: () -> Unit,
    onSettings: () -> Unit,
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(dims.spacing.s16),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        item {
            PMRowItem(
                title = stringResource(R.string.admin_moderation_title),
                leadingIcon = Icons.AutoMirrored.Filled.Comment,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Single,
                onClick = onPendingComments
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_comment_reports_title),
                leadingIcon = Icons.Filled.Flag,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Single,
                onClick = onCommentReports
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_plate_removal_title),
                leadingIcon = Icons.Filled.RateReview,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Single,
                onClick = onPlateRemoval
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_hidden_plates_title),
                leadingIcon = Icons.Filled.VisibilityOff,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Single,
                onClick = onHiddenPlates
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_report_types_title),
                leadingIcon = Icons.AutoMirrored.Filled.Label,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                onClick = onReportTypes
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_social_platforms_title),
                leadingIcon = Icons.Filled.Share,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                onClick = onSocialPlatforms
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_premium_plans_title),
                leadingIcon = Icons.Filled.WorkspacePremium,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                onClick = onPremiumPlans
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_premium_features_title),
                leadingIcon = Icons.Filled.Star,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                onClick = onPremiumFeatures
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_theme_colors_title),
                leadingIcon = Icons.Filled.Palette,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                onClick = onThemeColors
            )
        }

        item {
            PMRowItem(
                title = stringResource(R.string.admin_settings_title),
                leadingIcon = Icons.Filled.Tune,
                leadingIconTint = colors.primary,
                leadingContainerColor = colors.primaryContainer,
                position = PMRowPosition.Single,
                onClick = onSettings
            )
        }
    }
}

@Preview(name = "AdminHub Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun AdminHubScreenLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        AdminHubScreen(
            onPendingComments = {},
            onCommentReports = {},
            onPlateRemoval = {},
            onHiddenPlates = {},
            onReportTypes = {},
            onSocialPlatforms = {},
            onPremiumPlans = {},
            onPremiumFeatures = {},
            onThemeColors = {},
            onSettings = {},
            contentPadding = PaddingValues(0.dp)
        )
    }
}

@Preview(name = "AdminHub Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun AdminHubScreenDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        AdminHubScreen(
            onPendingComments = {},
            onCommentReports = {},
            onPlateRemoval = {},
            onHiddenPlates = {},
            onReportTypes = {},
            onSocialPlatforms = {},
            onPremiumPlans = {},
            onPremiumFeatures = {},
            onThemeColors = {},
            onSettings = {},
            contentPadding = PaddingValues(0.dp)
        )
    }
}
