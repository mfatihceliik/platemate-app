package com.mefy.platemate.presentation.features.main.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import com.mefy.platemate.R
import com.mefy.platemate.presentation.components.PMText
import com.mefy.platemate.presentation.features.main.profile.model.ProfileReviewStatusUi
import com.mefy.platemate.presentation.features.main.profile.model.StatusPillStyle
import com.mefy.platemate.presentation.theme.PlateMateTheme
import com.mefy.platemate.presentation.theme.pmColors
import com.mefy.platemate.presentation.theme.pmDimensions

@Composable
fun StatusPill(
    status: ProfileReviewStatusUi,
    createdAtText: String,
    modifier: Modifier = Modifier
) {
    val dims = MaterialTheme.pmDimensions
    val colors = MaterialTheme.pmColors

    val style = run {
        when (status) {
            ProfileReviewStatusUi.APPROVED -> StatusPillStyle(
                label = R.string.profile_review_status_approved,
                background = colors.categoryGreenBg,
                foreground = colors.categoryGreenFg
            )
            ProfileReviewStatusUi.PENDING_REVIEW -> StatusPillStyle(
                label = R.string.profile_review_status_pending_review,
                background = colors.categoryOrangeBg,
                foreground = colors.categoryOrangeFg
            )
            ProfileReviewStatusUi.REJECTED -> StatusPillStyle(
                label = R.string.profile_review_status_rejected,
                background = colors.errorContainer,
                foreground = colors.onErrorContainer
            )
            ProfileReviewStatusUi.REMOVED_BY_USER -> StatusPillStyle(
                label = R.string.profile_review_status_removed_by_user,
                background = colors.categoryIndigoBg,
                foreground = colors.categoryIndigoFg
            )
            ProfileReviewStatusUi.REMOVED_BY_MODERATOR -> StatusPillStyle(
                label = R.string.profile_review_status_removed_by_moderator,
                background = colors.categoryIndigoBg,
                foreground = colors.categoryIndigoFg
            )
            ProfileReviewStatusUi.REMOVED_BY_LEGAL_REQUEST -> StatusPillStyle(
                label = R.string.profile_review_status_removed_by_legal_request,
                background = colors.categoryIndigoBg,
                foreground = colors.categoryIndigoFg
            )
            ProfileReviewStatusUi.UNKNOWN -> StatusPillStyle(
                label = R.string.profile_review_status_unknown,
                background = colors.surfaceVariant,
                foreground = colors.textSecondary
            )
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(style.background, RoundedCornerShape(dims.radius.rFull))
                .padding(horizontal = dims.spacing.s12, vertical = dims.spacing.s4)
        ) {
            PMText(
                text = stringResource(style.label),
                fontSize = dims.fontSize.sm,
                fontWeight = FontWeight.Medium,
                color = style.foreground
            )
        }
        PMText(
            text = createdAtText,
            fontSize = dims.fontSize.sm,
            color = colors.textLabel
        )
    }
}

@Preview(name = "StatusPill Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun StatusPillLightPreview() {
    PlateMateTheme(darkTheme = false, dynamicColor = false) {
        StatusPillPreviewContent()
    }
}

@Preview(name = "StatusPill Dark", showBackground = true, backgroundColor = 0xFF0F172A)
@Composable
private fun StatusPillDarkPreview() {
    PlateMateTheme(darkTheme = true, dynamicColor = false) {
        StatusPillPreviewContent()
    }
}

@Composable
private fun StatusPillPreviewContent() {
    val dims = MaterialTheme.pmDimensions
    Column(
        modifier = Modifier.padding(dims.spacing.s16),
        verticalArrangement = Arrangement.spacedBy(dims.spacing.s8)
    ) {
        StatusPill(status = ProfileReviewStatusUi.APPROVED, createdAtText = "2026-05-27")
        StatusPill(status = ProfileReviewStatusUi.PENDING_REVIEW, createdAtText = "2026-05-26")
        StatusPill(status = ProfileReviewStatusUi.REJECTED, createdAtText = "2026-05-25")
    }
}