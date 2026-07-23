package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.components.PMStatPill
import com.mefy.platemate.presentation.components.variant.PMCardVariant
import com.mefy.platemate.presentation.features.uimodel.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.theme.PMTheme
import com.mefy.platemate.presentation.theme.PlateMateTheme

@Composable
internal fun ProfileStatusSummarySection(
    statusSummary: ProfileStatusSummaryUiModel,
    modifier: Modifier = Modifier,
    onStatusClick: (String) -> Unit = {}
) {
    val colors = PMTheme.colors
    val spacing = PMTheme.spacing

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(spacing.s16)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.s12)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing.s8)
            ) {
                PMStatPill(
                    label = stringResource(R.string.profile_status_approved),
                    value = statusSummary.approved.toString(),
                    dotColor = colors.success,
                    variant = PMCardVariant.Large,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusClick("APPROVED") }
                )
                PMStatPill(
                    label = stringResource(R.string.profile_status_pending_review),
                    value = statusSummary.pendingReview.toString(),
                    dotColor = colors.warning,
                    variant = PMCardVariant.Large,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusClick("PENDING_REVIEW") }
                )
                PMStatPill(
                    label = stringResource(R.string.profile_status_rejected),
                    value = statusSummary.rejected.toString(),
                    dotColor = colors.error,
                    variant = PMCardVariant.Large,
                    modifier = Modifier.weight(1f),
                    onClick = { onStatusClick("REJECTED") }
                )
            }
        }
    }
}

@Preview(name = "StatusSummary Light", showBackground = true, backgroundColor = 0xFFF6F8FB)
@Composable
private fun ProfileStatusSummarySectionLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        ProfileStatusSummarySectionPreviewContent()
    }
}

@Preview(name = "StatusSummary Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun ProfileStatusSummarySectionDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        ProfileStatusSummarySectionPreviewContent()
    }
}

@Composable
private fun ProfileStatusSummarySectionPreviewContent() {
    val spacing = PMTheme.spacing
    ProfileStatusSummarySection(
        statusSummary = ProfileStatusSummaryUiModel(approved = 124, pendingReview = 12, rejected = 6),
        modifier = Modifier.padding(spacing.s16)
    )
}