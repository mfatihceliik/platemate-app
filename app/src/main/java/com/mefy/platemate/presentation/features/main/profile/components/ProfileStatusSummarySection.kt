package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMCard
import com.mefy.platemate.presentation.features.main.profile.model.ProfileStatusSummaryUiModel
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun ProfileStatusSummarySection(
    statusSummary: ProfileStatusSummaryUiModel,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    PMCard(
        modifier = modifier.fillMaxWidth(),
        padding = PaddingValues(dims.spacing.s16)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dims.spacing.s12)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dims.spacing.s8)
            ) {
                StatusCountPill(
                    label = stringResource(R.string.profile_status_approved),
                    value = statusSummary.approved.toString(),
                    dotColor = colors.success,
                    modifier = Modifier.weight(1f)
                )
                StatusCountPill(
                    label = stringResource(R.string.profile_status_pending_review),
                    value = statusSummary.pendingReview.toString(),
                    dotColor = colors.warning,
                    modifier = Modifier.weight(1f)
                )
                StatusCountPill(
                    label = stringResource(R.string.profile_status_rejected),
                    value = statusSummary.rejected.toString(),
                    dotColor = colors.error,
                    modifier = Modifier.weight(1f)
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
    val dims = MaterialTheme.pmDimensions
    ProfileStatusSummarySection(
        statusSummary = ProfileStatusSummaryUiModel(approved = 124, pendingReview = 12, rejected = 6),
        modifier = Modifier.padding(dims.spacing.s16)
    )
}